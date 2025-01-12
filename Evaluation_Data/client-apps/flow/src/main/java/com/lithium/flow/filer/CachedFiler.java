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
import com.lithium.flow.io.DecoratedOutputStream;
import com.lithium.flow.util.Caches;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.google.common.cache.LoadingCache;

/**
 * Decorates an instance of {@link Filer} to cache records for a specified amount of time.
 *
 * @author Matt Ayres
 */
public class CachedFiler extends DecoratedFiler {
	private final LoadingCache<String, List<Record>> dirCache;
	private final LoadingCache<String, Record> fileCache;
	private final boolean dirOnly;

	public CachedFiler(@Nonnull Filer delegate, @Nonnull Config config) {
		super(checkNotNull(delegate));
		checkNotNull(config);

		int concurrency = config.getInt("cache.concurrency", 4);
		long expireTime = config.getTime("cache.expireTime", "1m");
		dirOnly = config.getBoolean("cache.dirOnly", false);

		dirCache = Caches.build(delegate::listRecords,
				b -> b.softValues().concurrencyLevel(concurrency).expireAfterWrite(expireTime, TimeUnit.MILLISECONDS));
		fileCache = Caches.build(delegate::getRecord,
				b -> b.softValues().concurrencyLevel(concurrency).expireAfterWrite(expireTime, TimeUnit.MILLISECONDS));
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		return Caches.get(dirCache, path, IOException.class);
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		if (dirOnly) {
			String parent = RecordPath.getFolder(path);
			for (Record record : listRecords(parent)) {
				if (path.equals(record.getPath())) {
					return record;
				}
			}

			return Record.noFile(getUri(), path);
		}

		return Caches.get(fileCache, path, IOException.class);
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		checkNotNull(path);
		if (!getRecord(path).exists()) {
			super.createDirs(path);
			dirCache.invalidate(path);
		}
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		checkNotNull(path);
		return new DecoratedOutputStream(super.writeFile(path)) {
			@Override
			public void close() throws IOException {
				super.close();
				dirCache.invalidate(RecordPath.getFolder(path));
				fileCache.invalidate(path);
			}
		};
	}

	@Override
	public void close() throws IOException {
		dirCache.invalidateAll();
		fileCache.invalidateAll();
		super.close();
	}
}
