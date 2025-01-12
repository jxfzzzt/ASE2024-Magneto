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
import static com.google.common.io.BaseEncoding.base16;

import com.lithium.flow.config.Config;
import com.lithium.flow.util.JedisUtils;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.crypto.spec.SecretKeySpec;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Matt Ayres
 */
public class RedisKeySource implements KeySource {
	private final JedisPool jedisPool;
	private final String redisKey;
	private final boolean generate;

	public RedisKeySource(@Nonnull Config config) {
		checkNotNull(config);
		jedisPool = JedisUtils.buildPool(config.prefix("key"));
		redisKey = config.getString("key.redis.key");
		generate = config.getBoolean("key.generate", true);
	}

	@Override
	@Nonnull
	public List<Key> getKeys(@Nonnull String name) {
		checkNotNull(name);
		try (Jedis jedis = jedisPool.getResource()) {
			String hexKey = jedis.hget(redisKey, name);
			if (hexKey == null) {
				if (generate) {
					jedis.hsetnx(redisKey, name, keyGen());
					hexKey = jedis.hget(redisKey, name);
				} else {
					throw new IllegalArgumentException("no key for name: " + name);
				}
			}
			return Collections.singletonList(new SecretKeySpec(base16().decode(hexKey), "AES"));
		}
	}

	@Nonnull
	private String keyGen() {
		byte[] bytes = new byte[16];
		new SecureRandom().nextBytes(bytes);
		return base16().encode(bytes);
	}
}
