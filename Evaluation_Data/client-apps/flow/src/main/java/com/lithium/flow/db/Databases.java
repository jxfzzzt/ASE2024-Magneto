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

import java.util.function.Function;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Databases {
	@Nonnull
	public static Database buildDatabase(@Nonnull Repo repo) {
		checkNotNull(repo);
		return buildDatabase(repo, config -> config);
	}

	@Nonnull
	public static Database buildDatabase(@Nonnull Repo repo, @Nonnull Function<Config, Config> function) {
		checkNotNull(repo);
		checkNotNull(function);
		return new RepoDatabase(repo, config -> buildSchema(function.apply(config)));
	}

	@Nonnull
	public static Schema buildSchema(@Nonnull Config config) {
		checkNotNull(config);
		return new TomcatSchema(config);
	}
}
