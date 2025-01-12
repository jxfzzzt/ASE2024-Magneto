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

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.io.Swallower;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class Recycler<K, V extends Closeable> implements Closeable {
	private static final Logger log = Logs.getLogger();

	private final long time;
	private final LoadingCache<K, Bin> cache;
	private volatile boolean closing;

	public Recycler(@Nonnull Config config, @Nonnull CheckedFunction<K, V, Exception> function) {
		checkNotNull(config);
		checkNotNull(function);

		time = config.getTime("recycle.time", "1m");

		cache = Caches.buildWithListener(key -> new Bin(function.apply(key)),
				builder -> builder.expireAfterAccess(time, TimeUnit.MILLISECONDS),
				notification -> Swallower.close(notification.getValue()));
	}

	@Nonnull
	public Reusable<V> get(@Nonnull K key) throws IOException {
		checkNotNull(key);
		return Caches.get(cache, key, IOException.class);
	}

	@Override
	public void close() {
		closing = true;
		cache.invalidateAll();
	}

	private class Bin implements Reusable<V>, Closeable {
		private final V value;
		private final Set<Object> set = new HashSet<>();
		private boolean closed;

		public Bin(@Nonnull V value) {
			this.value = checkNotNull(value);
		}

		@Override
		@Nonnull
		public synchronized V get(@Nonnull Object object) {
			set.add(object);
			return value;
		}

		@Override
		public synchronized void recycle(@Nonnull Object object) {
			set.remove(object);
		}

		@Override
		public synchronized void close() throws IOException {
			if (!closed) {
				if (closing || set.isEmpty()) {
					value.close();
				} else {
					Execute.in(time, this::close);
				}
				closed = true;
			}
		}
	}
}
