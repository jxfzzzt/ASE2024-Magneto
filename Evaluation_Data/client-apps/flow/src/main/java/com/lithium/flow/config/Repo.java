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

package com.lithium.flow.config;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public interface Repo extends Closeable {
	@Nonnull
	List<String> getNames() throws IOException;

	@Nonnull
	Config getConfig(@Nonnull String name) throws IOException;

	@Nonnull
	default List<Config> getConfigs() throws IOException {
		List<Config> configs = new ArrayList<>();
		for (String name : getNames()) {
			configs.add(getConfig(name));
		}
		return configs;
	}

	@Nonnull
	default Stream<Config> streamConfigs() throws IOException {
		return getConfigs().stream();
	}
}
