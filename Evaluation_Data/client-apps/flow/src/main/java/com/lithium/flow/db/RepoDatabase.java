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

package com.lithium.flow.db;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;
import com.lithium.flow.io.Swallower;
import com.lithium.flow.util.Caches;

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class RepoDatabase implements Database {
	private final LoadingCache<String, Schema> schemas;

	public RepoDatabase(@Nonnull Repo repo, @Nonnull Function<Config, Schema> function) {
		checkNotNull(repo);
		checkNotNull(function);
		schemas = Caches.build(name -> function.apply(repo.getConfig(name)));
	}

	@Override
	@Nonnull
	public Schema getSchema(@Nonnull String name) {
		return schemas.getUnchecked(name);
	}

	@Override
	public void close() {
		schemas.asMap().values().forEach(Swallower::close);
	}
}
