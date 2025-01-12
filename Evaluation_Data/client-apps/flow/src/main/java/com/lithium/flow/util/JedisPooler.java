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

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Matt Ayres
 */
public class JedisPooler extends JedisPool {
	public JedisPooler(GenericObjectPoolConfig poolConfig, String host, int port, int timeout) {
		super(poolConfig, host, port, timeout);
	}

	public <T> T apply(@Nonnull Function<Jedis, T> function) {
		checkNotNull(function);
		try (Jedis jedis = getResource()) {
			return function.apply(jedis);
		}
	}

	public void accept(@Nonnull Consumer<Jedis> consumer) {
		checkNotNull(consumer);
		try (Jedis jedis = getResource()) {
			consumer.accept(jedis);
		}
	}
}
