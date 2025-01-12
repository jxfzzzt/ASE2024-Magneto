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

package com.lithium.flow.config.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;
import com.lithium.flow.util.Caches;
import com.lithium.flow.util.Checker;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.LoopThread;
import com.lithium.flow.util.Sleep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

/**
 * @author Matt Ayres
 */
public class ScheduledRepo implements Repo {
	private static final Logger log = Logs.getLogger();

	private final CountDownLatch latch = new CountDownLatch(1);
	private final Thread thread;
	private volatile Map<String, Config> configMap;
	private final LoadingCache<String, Config> configCache;

	public ScheduledRepo(@Nonnull Repo delegate, long interval, long offset) {
		this(delegate, interval, offset, () -> true);
	}

	public ScheduledRepo(@Nonnull Repo delegate, long interval, long offset, @Nonnull Checker checker) {
		configCache = Caches.build(delegate::getConfig);

		thread = new LoopThread(interval, offset - interval * 2, true, () -> {
			if (configMap == null || checker.check()) {
				try {
					configMap = delegate.streamConfigs().collect(toMap(Config::getName, config -> config));
					configMap.keySet().forEach(configCache::invalidate);
					latch.countDown();
				} catch (IOException e) {
					log.warn("failed to read configs", e);
				}
			}
		});
	}

	@Override
	@Nonnull
	public List<String> getNames() {
		return new ArrayList<>(getConfigMap().keySet());
	}

	@Override
	@Nonnull
	public Config getConfig(@Nonnull String name) throws IOException {
		checkNotNull(name);
		Config config = getConfigMap().get(name);
		if (config == null) {
			return Caches.get(configCache, name, IOException.class);
		}
		return config;
	}

	@Override
	@Nonnull
	public List<Config> getConfigs() {
		return Lists.newArrayList(getConfigMap().values());
	}

	@Nonnull
	@Override
	public Stream<Config> streamConfigs() {
		return getConfigMap().values().stream();
	}

	@Nonnull
	private Map<String, Config> getConfigMap() {
		return configMap != null || Sleep.softly(latch::await) ? configMap : Collections.emptyMap();
	}

	@Override
	public void close() {
		thread.interrupt();
	}
}
