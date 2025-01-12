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

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;

import com.lithium.flow.util.JedisPooler;

import java.util.Map;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.google.common.collect.Queues;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

/**
 * @author Matt Ayres
 */
public class RedisTable implements Table {
	private final JedisPooler pooler;

	public RedisTable(@Nonnull JedisPooler pooler) {
		this.pooler = checkNotNull(pooler);
	}

	@Override
	@Nonnull
	public Row getRow(@Nonnull Key key) {
		Map<String, ?> map = pooler.apply(j -> j.hgetAll(key.id()));
		return new Row(key).putAll(map);
	}

	@Override
	public void putRow(@Nonnull Row row) {
		Map<String, String> map = row.columns().stream().collect(toMap(c -> c, c -> {
			Object value = row.getCell(c, Object.class);
			return value != null ? value.toString() : "";
		}));
		String id = row.getKey().id();
		pooler.apply(j -> j.hmset(id, map));
	}

	@Override
	public void deleteRow(@Nonnull Key key) {
		pooler.accept(j -> j.hdel(key.id()));
	}

	@Override
	@Nonnull
	public Stream<Row> rows() {
		Jedis jedis = pooler.getResource();
		AtomicReference<String> cursorRef = new AtomicReference<>("0");
		AtomicReference<Queue<String>> queueRef = new AtomicReference<>();

		return StreamSupport.stream(new Spliterator<Row>() {
			@Override
			public boolean tryAdvance(Consumer<? super Row> action) {
				Queue<String> queue = queueRef.get();
				if (queue == null || queue.isEmpty()) {
					ScanResult<String> result = jedis.scan(cursorRef.get());
					if (result.getResult().isEmpty()) {
						jedis.close();
						return false;
					}

					cursorRef.set(result.getCursor());

					queue = Queues.newArrayDeque(result.getResult());
					queueRef.set(queue);
				}

				String value = queue.remove();
				Key key = Key.from(value);
				Row row = getRow(key);

				action.accept(row);
				return true;
			}

			@Override
			public Spliterator<Row> trySplit() {
				return null;
			}

			@Override
			public long estimateSize() {
				return Long.MAX_VALUE;
			}

			@Override
			public int characteristics() {
				return IMMUTABLE;
			}
		}, false);
	}
}
