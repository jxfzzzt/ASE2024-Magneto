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

import com.lithium.flow.util.Caches;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

/**
 * Decorates an instance of {@link Filer} to cache file reads in memory.
 * Cached entries are only invalidated when the file time changes.
 *
 * @author Matt Ayres
 */
public class CachedReadFiler extends DecoratedFiler {
	private final LoadingCache<String, Pair<Long, byte[]>> cache;

	public CachedReadFiler(@Nonnull Filer delegate) {
		super(checkNotNull(delegate));
		cache = Caches.build(path -> {
			try (InputStream in = delegate.readFile(path)) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				IOUtils.copy(in, out);
				return Pair.of(delegate.getRecord(path).getTime(), out.toByteArray());
			}
		}, CacheBuilder::softValues);
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		checkNotNull(path);

		Pair<Long, byte[]> pair = cache.getIfPresent(path);
		if (pair != null) {
			if (pair.getLeft().equals(super.getRecord(path).getTime())) {
				return new ByteArrayInputStream(pair.getRight());
			} else {
				cache.invalidate(path);
			}
		}

		return new ByteArrayInputStream(Caches.get(cache, path, IOException.class).getRight());
	}

	@Override
	public void close() throws IOException {
		cache.invalidateAll();
		super.close();
	}
}
