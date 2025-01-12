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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.ioc.Chain;
import com.lithium.flow.ioc.Locator;
import com.lithium.flow.ioc.PicoLocator;
import com.lithium.flow.ioc.Provider;
import com.lithium.flow.streams.ChainedStreamer;
import com.lithium.flow.streams.Streamer;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class FilerFactory {
	private final Map<String, Class<? extends Filer>> schemes = new ConcurrentHashMap<>();
	private final Map<String, Class<? extends Streamer>> streams = new ConcurrentHashMap<>();
	private final Map<String, Class<? extends Chain<Filer>>> chains = new ConcurrentHashMap<>();
	private final Map<Class<?>, Provider<?>> providers = new ConcurrentHashMap<>();

	@Nonnull
	public FilerFactory addScheme(@Nonnull String scheme, @Nonnull Class<? extends Filer> type) {
		schemes.put(scheme, type);
		return this;
	}

	@Nonnull
	public FilerFactory addStreamer(@Nonnull String stream, @Nonnull Class<? extends Streamer> type) {
		streams.put(stream, type);
		return this;
	}

	@Nonnull
	public FilerFactory addChain(@Nonnull String chain, @Nonnull Class<? extends Chain<Filer>> type) {
		chains.put(chain, type);
		return this;
	}

	@Nonnull
	public FilerFactory addProvider(@Nonnull Class<?> type, @Nonnull Provider<?> provider) {
		providers.put(type, provider);
		return this;
	}

	@Nonnull
	public FilerFactory attempt(@Nonnull UnaryOperator<FilerFactory> op) {
		try {
			return op.apply(this);
		} catch (NoClassDefFoundError e) {
			return this;
		}
	}

	@Nonnull
	public Filer buildFiler(@Nonnull Config config, Object... provided) throws IOException {
		checkNotNull(config);
		config = config.prefix("filer");

		Locator locator = new PicoLocator();
		locator.addInstance(config);
		Arrays.asList(provided).forEach(locator::addInstance);
		providers.forEach(locator::addProvider);

		String url = config.getString("url");
		URI uri = URI.create(url);
		Class<? extends Filer> type = schemes.get(uri.getScheme());
		if (type == null) {
			throw new IllegalArgumentException("scheme not defined: " + uri.getScheme() + ", for url: " + url);
		}
		locator.addType(type);

		Filer filer = locator.getInstance(type);
		for (Chain<Filer> chain : buildChains(config, locator)) {
			try {
				filer = chain.chain(filer);
			} catch (Exception e) {
				throw new IOException("failed to chain filer: " + chain.getClass().getName(), e);
			}
		}

		List<Streamer> streamers = buildStreamers(config, locator);
		return streamers.isEmpty() ? filer : new StreamerFiler(filer, new ChainedStreamer(streamers));
	}

	@Nonnull
	private List<Streamer> buildStreamers(@Nonnull Config config, @Nonnull Locator locator) {
		return config.getList("streamers", Configs.emptyList()).stream().map(stream -> {
			Class<? extends Streamer> type = streams.get(stream);
			if (type == null) {
				throw new IllegalArgumentException("streamer not defined: " + stream);
			}
			locator.addType(type);
			return (Streamer) locator.getInstance(type);
		}).collect(Collectors.toList());
	}

	@Nonnull
	private List<Chain<Filer>> buildChains(@Nonnull Config config, @Nonnull Locator locator) {
		return config.getList("chains", Configs.emptyList()).stream().map(chain -> {
			Class<? extends Chain<Filer>> type = chains.get(chain);
			if (type == null) {
				throw new IllegalArgumentException("chain not defined: " + chain);
			}
			locator.addType(type);
			return (Chain<Filer>) locator.getInstance(type);
		}).collect(Collectors.toList());
	}
}
