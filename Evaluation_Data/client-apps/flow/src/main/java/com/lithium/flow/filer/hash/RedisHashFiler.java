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

package com.lithium.flow.filer.hash;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.DecoratedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.util.JedisPooler;
import com.lithium.flow.util.JedisUtils;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import redis.clients.jedis.Jedis;

/**
 * @author Matt Ayres
 */
public class RedisHashFiler extends DecoratedFiler {
	private final JedisPooler pooler;
	private final String prefix;
	private final int expire;

	public RedisHashFiler(@Nonnull Filer delegate, @Nonnull Config config) {
		super(checkNotNull(delegate));
		checkNotNull(config);
		pooler = JedisUtils.buildPooler(config);
		prefix = config.getString("prefix", "");
		expire = config.getString("expire", "-1").equals("-1") ? -1 : (int) (config.getTime("expire") / 1000);
	}

	@Override
	@Nonnull
	public String getHash(@Nonnull String path, @Nonnull String hash, @Nonnull String base) throws IOException {
		String key = prefix + path;

		try (Jedis jedis = pooler.getResource()) {
			String value = jedis.hget(key, hash);
			if (value == null) {
				value = super.getHash(key, hash, base);
				jedis.hset(key, hash, value);
				if (expire > -1) {
					jedis.expire(key, expire);
				}
			}
			return value;
		}
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		pooler.accept(j -> j.del(path));
		return super.writeFile(path);
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		pooler.accept(j -> j.del(path));
		super.deleteFile(path);
	}

	@Override
	public void close() throws IOException {
		try {
			pooler.close();
		} finally {
			super.close();
		}
	}
}
