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

package com.lithium.flow.table;

import com.lithium.flow.util.Caches;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class MemoryTable implements Table {
	private final LoadingCache<Key, Row> cache = Caches.build(Row::new);

	@Override
	@Nonnull
	public Row getRow(@Nonnull Key key) {
		return cache.getUnchecked(key);
	}

	@Override
	public void putRow(@Nonnull Row row) {
		cache.put(row.getKey(), row);
	}

	@Override
	public void deleteRow(@Nonnull Key key) {
		cache.invalidate(key);
	}

	@Override
	@Nonnull
	public Stream<Key> keys() {
		return cache.asMap().keySet().stream();
	}

	@Override
	@Nonnull
	public Stream<Row> rows() {
		return cache.asMap().values().stream();
	}

	@Override
	public void close() {
		cache.invalidateAll();
	}
}
