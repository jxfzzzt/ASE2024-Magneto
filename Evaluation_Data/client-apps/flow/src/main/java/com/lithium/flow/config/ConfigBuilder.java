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

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Builder for {@link Config} instances.
 *
 * @author Matt Ayres
 */
public interface ConfigBuilder {
	/**
	 * @return a new {@link Config} instance initialized from the properties set into the builder. Never {@code null}.
	 */
	@Nonnull
	Config build();

	@Nonnull
	ConfigBuilder pushPrefix(@Nonnull String prefix);

	@Nonnull
	ConfigBuilder popPrefix();

	/**
	 * @param path the path from which to load configs.
	 * @return this builder with the configs at the path included.
	 * @throws java.io.IOException if the loader throws and exception while reading configs from the path,
	 *                             or the path does not identify a config or resource.
	 */
	@Nonnull
	ConfigBuilder include(@Nonnull String path) throws IOException;

	/**
	 * Adds a config for a {@code (key, value)}.
	 *
	 * @param key   the config key. Cannot be {@code null}.
	 * @param value the config value. Cannot be {@code null}.
	 * @return this builder with the new config added. Never {@code null}.
	 */
	@Nonnull
	ConfigBuilder setString(@Nonnull String key, @Nonnull String value);

	/**
	 * Set all {@code (key, value)} pairs. All pairs with {@code null} keys or values are ignored.
	 *
	 * @param configValues the config values to add. Cannot be {@code null}.
	 * @return this builder with the new configs added. Never {@code null}.
	 */
	@Nonnull
	ConfigBuilder setAll(@Nonnull Map<String, String> configValues);

	/**
	 * Adds all the (key, value) pairs from the specified {@link Config}.
	 *
	 * @param newConfig the config values to add. Cannot be {@code null}.
	 * @return this builder with the new configs added. Never {@code null}.
	 */
	@Nonnull
	ConfigBuilder addAll(@Nonnull Config newConfig);

	/**
	 * Removes a config for a key.
	 *
	 * @param key the config key. Cannot be {@code null}.
	 * @return this builder with the config removed. Never {@code null}.
	 */
	@Nonnull
	ConfigBuilder removeKey(@Nonnull String key);

	/**
	 * Requires a config for a key at build time. The {@link #build()} method should throw an exception
	 * if this key is not available by that time.
	 */
	@Nonnull
	ConfigBuilder requireKey(@Nonnull String key);

	/**
	 * Returns true if the builder contains the specified key.
	 *
	 * @param key the config key to check. Cannot be {@code null}.
	 * @return true if the config being built contains the key.
	 */
	boolean containsKey(@Nonnull String key);

	/**
	 * Gets the current parsed value for a key.
	 *
	 * @param key the config key. Cannot be {@code null}.
	 * @return the parsed config value. Can be {@code null}.
	 */
	@Nullable
	String getString(@Nonnull String key);

	@Nonnull
	ConfigBuilder addLoader(@Nonnull ConfigLoader loader);

	@Nonnull
	ConfigBuilder resetLoaders();

	@Nonnull
	ConfigBuilder addParser(@Nonnull ConfigParser parser);

	@Nonnull
	ConfigBuilder resetParsers();

	@Nonnull
	ConfigBuilder addWatcher(@Nonnull ConfigWatcher watcher);

	@Nonnull
	ConfigBuilder allowFileNotFound(boolean allowFileNotFound);

	@Nonnull
	ConfigBuilder allowUndefined(boolean allowUndefined);

	@Nonnull
	ConfigBuilder allowRequiredKeys(boolean allowRequiredKeys);

	@Nonnull
	ConfigBuilder setName(@Nonnull String name);
}
