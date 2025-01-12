/*
 * Copyright 2015 Lithium Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lithium.flow.jetty;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.util.ConfigObjectPool;
import com.lithium.flow.util.HostUtils;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Unchecked;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;

import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
@WebSocket
public class JettyClient extends BasePooledObjectFactory<Session> implements Closeable {
	private static final Logger log = Logs.getLogger();

	private final Map<Session, Token<?>> tokens = new ConcurrentHashMap<>();
	private final AtomicInteger pos = new AtomicInteger();
	private final WebSocketClient client;
	private final ObjectPool<Session> pool;
	private final List<String> urls;
	private final long timeout;

	public JettyClient(@Nonnull Config config) {
		checkNotNull(config);

		client = new WebSocketClient(new HttpClient(new SslContextFactory.Client()));
		client.getPolicy().setMaxTextMessageSize(config.getInt("maxTextMessageSize", 1024 * 1024));
		client.getPolicy().setIdleTimeout(config.getTime("idleTimeout", "1h"));
		client.getPolicy().setAsyncWriteTimeout(config.getTime("asyncWriteTimeout", "1m"));
		client.getHttpClient().setConnectTimeout(config.getTime("connectTimeout", "15s"));
		Unchecked.run(client::start);

		pool = new ConfigObjectPool<>(this, config);

		urls = HostUtils.expand(config.getList("url", Splitter.on(' ')));
		timeout = config.getTime("timeout", "15s");
	}

	@Override
	public Session create() throws Exception {
		String url = urls.get(pos.getAndIncrement() % urls.size());
		log.debug("connecting: {}", url);
		return client.connect(this, new URI(url), new ClientUpgradeRequest()).get();
	}

	@Override
	public PooledObject<Session> wrap(@Nonnull Session session) {
		return new DefaultPooledObject<>(session);
	}

	@Override
	public void destroyObject(@Nonnull PooledObject<Session> pooled) {
		Session session = pooled.getObject();
		session.close();
	}

	@Override
	public boolean validateObject(@Nonnull PooledObject<Session> pooled) {
		Session session = pooled.getObject();
		return session.isOpen();
	}

	@Nonnull
	public String call(@Nonnull String text) throws IOException {
		return call(text, input -> input);
	}

	@Nonnull
	public <T> T call(@Nonnull String text, @Nonnull Decoder<T> decoder) throws IOException {
		checkNotNull(text);
		checkNotNull(decoder);

		Session session = Unchecked.get(pool::borrowObject);

		try {
			Token<T> token = new Token<>(decoder);
			tokens.put(session, token);
			session.getRemote().sendStringByFuture(text);
			return token.get(timeout);
		} finally {
			tokens.remove(session);
			Unchecked.run(() -> pool.returnObject(session));
		}
	}

	@OnWebSocketMessage
	public void onInput(@Nonnull Session session, @Nonnull String input) {
		log.trace("input: {}", input);

		Token<?> token = tokens.remove(session);

		if (token == null) {
			// message was late and call has already timed out
			return;
		}

		token.process(input);
	}

	@Override
	public void close() throws IOException {
		try {
			client.stop();
		} catch (Exception e) {
			throw new IOException("failed to close web socket client", e);
		}
	}
}
