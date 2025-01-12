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

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;
import com.lithium.flow.util.Caches;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class CachedRepo implements Repo {
	private final LoadingCache<String, List<String>> namesCache;
	private final LoadingCache<String, Config> configCache;

	public CachedRepo(@Nonnull Repo delegate, long duration) {
		this(delegate, duration, TimeUnit.MILLISECONDS);
	}

	public CachedRepo(@Nonnull Repo delegate, long duration, @Nonnull TimeUnit unit) {
		checkNotNull(delegate);
		checkNotNull(unit);

		namesCache = Caches.build(key -> delegate.getNames(), b -> b.expireAfterWrite(duration, unit));
		configCache = Caches.build(delegate::getConfig, b -> b.expireAfterWrite(duration, unit));
	}

	@Override
	@Nonnull
	public List<String> getNames() throws IOException {
		return Caches.get(namesCache, "", IOException.class);
	}

	@Override
	@Nonnull
	public Config getConfig(@Nonnull String name) throws IOException {
		return Caches.get(configCache, name, IOException.class);
	}

	@Override
	public void close() {
		namesCache.invalidateAll();
		configCache.invalidateAll();
	}
}
