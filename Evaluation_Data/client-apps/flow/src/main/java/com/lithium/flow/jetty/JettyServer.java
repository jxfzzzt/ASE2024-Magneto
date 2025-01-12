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
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public abstract class JettyServer {
	private static final Logger log = Logs.getLogger();

	@OnWebSocketConnect
	public void onConnect(@Nonnull Session session) {
		checkNotNull(session);
		log.debug("{} connected", session.getRemoteAddress());
	}

	@OnWebSocketMessage
	public void onMessage(@Nonnull Session session, @Nonnull String input) {
		checkNotNull(session);
		checkNotNull(input);

		log.debug("{} input: {}", session.getRemoteAddress(), input);

		try {
			session.getRemote().sendStringByFuture(encode(session, input));
		} catch (Exception e) {
			log.info("failed on input: {}", input, e);
			session.getRemote().sendStringByFuture("ERROR: " + e.getMessage());
		}
	}

	@OnWebSocketClose
	public void onClose(@Nonnull Session session, int status, @Nullable String reason) {
		checkNotNull(session);
		log.debug("{} disconnected: {} {}", session.getRemoteAddress(), status, reason);

		try {
			shutdown();
		} catch (Exception e) {
			log.warn("failed on shutdown", e);
		}
	}

	protected void shutdown() throws IOException {
	}

	protected abstract String encode(@Nonnull Session session, @Nonnull String param) throws IOException;

	public static void start(@Nonnull Config config, @Nonnull WebSocketCreator creator) throws Exception {
		start(config, Collections.emptyList(), creator);
	}

	public static void start(@Nonnull Config config, @Nonnull List<Handler> extraHandlers,
			@Nonnull WebSocketCreator creator) throws Exception {
		checkNotNull(config);
		checkNotNull(creator);

		long idleTimeout = config.getTime("webserver.idleTimeout", "10m");
		int maxTextMessageSize = config.getInt("webserver.maxTextMessageSize", 1024 * 1024);

		HandlerList handlers = new HandlerList();
		handlers.addHandler(new WebSocketHandler() {
			@Override
			public void configure(WebSocketServletFactory factory) {
				factory.getPolicy().setIdleTimeout(idleTimeout);
				factory.getPolicy().setMaxTextMessageSize(maxTextMessageSize);
				factory.setCreator(creator);
			}
		});
		for (Handler handler : extraHandlers) {
			handlers.addHandler(handler);
		}

		Server server = new Server(config.getInt("webserver.port", 8034));
		server.setHandler(handlers);
		server.start();
	}
}
