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

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public class Configs {
	/**
	 * @return a new, empty config instance.
	 */
	@Nonnull
	public static Config empty() {
		return newBuilder().build();
	}

	/**
	 * @return a new {@link ConfigBuilder} instance.
	 */
	@Nonnull
	public static ConfigBuilder newBuilder() {
		return new BaseConfigBuilder();
	}

	/**
	 * @return a new {@link ConfigBuilder} instance.
	 */
	@Nonnull
	public static ConfigBuilder newBuilder(@Nullable Config defaults) {
		return new BaseConfigBuilder(defaults);
	}

	/**
	 * @return empty {@link List} typed for {@link String}, useful for {@link Config#getList(String, List)}.
	 */
	@Nonnull
	public static List<String> emptyList() {
		return Collections.emptyList();
	}
}
