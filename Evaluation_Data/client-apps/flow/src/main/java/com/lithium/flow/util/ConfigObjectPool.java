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

import javax.annotation.Nonnull;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author Matt Ayres
 */
public class ConfigObjectPool<T> extends GenericObjectPool<T> {
	public ConfigObjectPool(@Nonnull PooledObjectFactory<T> factory, @Nonnull Config config) {
		super(checkNotNull(factory), buildConfig(checkNotNull(config)));
	}

	@Nonnull
	public static <T> GenericObjectPoolConfig<T> buildConfig(@Nonnull Config config) {
		GenericObjectPoolConfig<T> pc = new GenericObjectPoolConfig<>();

		// GenericObjectPoolConfig
		pc.setMaxTotal(config.getInt("pool.maxTotal", pc.getMaxTotal()));
		pc.setMaxIdle(config.getInt("pool.maxIdle", pc.getMaxIdle()));
		pc.setMinIdle(config.getInt("pool.minIdle", pc.getMinIdle()));

		// BaseObjectPoolConfig
		pc.setLifo(config.getBoolean("pool.lifo", pc.getLifo()));
		pc.setFairness(config.getBoolean("pool.fairness", pc.getFairness()));
		pc.setMaxWaitMillis(config.getLong("pool.maxWaitMillis", pc.getMaxWaitMillis()));
		pc.setMinEvictableIdleTimeMillis(config.getLong("pool.minEvictableIdleTimeMillis",
				pc.getMinEvictableIdleTimeMillis()));
		pc.setSoftMinEvictableIdleTimeMillis(config.getLong("pool.softMinEvictableIdleTimeMillis",
				pc.getSoftMinEvictableIdleTimeMillis()));
		pc.setNumTestsPerEvictionRun(config.getInt("pool.numTestsPerEvictionRun", pc.getNumTestsPerEvictionRun()));
		pc.setTestOnCreate(config.getBoolean("pool.testOnCreate", pc.getTestOnCreate()));
		pc.setTestOnBorrow(config.getBoolean("pool.testOnBorrow", pc.getTestOnBorrow()));
		pc.setTestOnReturn(config.getBoolean("pool.testOnReturn", pc.getTestOnReturn()));
		pc.setTestWhileIdle(config.getBoolean("pool.testWhileIdle", pc.getTestWhileIdle()));
		pc.setTimeBetweenEvictionRunsMillis(config.getLong("pool.timeBetweenEvictionRunsMillis",
				pc.getTimeBetweenEvictionRunsMillis()));
		pc.setBlockWhenExhausted(config.getBoolean("pool.blockWhenExhausted", pc.getBlockWhenExhausted()));
		pc.setJmxEnabled(config.getBoolean("pool.jmxEnabled", pc.getJmxEnabled()));
		pc.setJmxNamePrefix(config.getString("pool.jmxNamePrefix", pc.getJmxNamePrefix()));
		pc.setJmxNameBase(config.getString("pool.jmxNameBase", pc.getJmxNameBase()));
		pc.setEvictionPolicyClassName(config.getString("pool.evictionPolicyClassName",
				pc.getEvictionPolicyClassName()));

		return pc;
	}
}
