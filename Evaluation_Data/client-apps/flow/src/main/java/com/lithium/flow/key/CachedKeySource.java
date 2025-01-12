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

package com.lithium.flow.key;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.util.Caches;

import java.security.Key;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class CachedKeySource implements KeySource {
	private final LoadingCache<String, List<Key>> cache;

	public CachedKeySource(@Nonnull KeySource delegate) {
		checkNotNull(delegate);
		cache = Caches.build(delegate::getKeys);
	}

	@Override
	@Nonnull
	public List<Key> getKeys(@Nonnull String name) {
		return cache.getUnchecked(name);
	}
}
