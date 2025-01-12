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

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

/**
 * @author Matt Ayres
 */
public interface Config {
	Pattern LIST_SPLIT_PATTERN = Pattern.compile(" *([ ,]) *");

	@Nonnull
	String getName();

	boolean isAllowUndefined();

	boolean containsKey(@Nonnull String key);

	String getRaw(@Nonnull String key);

	String getString(@Nonnull String key);

	String getString(@Nonnull String key, @Nullable String def);

	int getInt(@Nonnull String key);

	int getInt(@Nonnull String key, int def);

	long getLong(@Nonnull String key);

	long getLong(@Nonnull String key, long def);

	long getTime(@Nonnull String key);

	long getTime(@Nonnull String key, @Nullable String def);

	double getDouble(@Nonnull String key);

	double getDouble(@Nonnull String key, double def);

	boolean getBoolean(@Nonnull String key);

	boolean getBoolean(@Nonnull String key, boolean def);

	@Nonnull
	List<String> getList(@Nonnull String key);

	@Nonnull
	List<String> getList(@Nonnull String key, @Nonnull List<String> def);

	@Nonnull
	List<String> getList(@Nonnull String key, @Nonnull Splitter splitter);

	@Nonnull
	List<String> getList(@Nonnull String key, @Nonnull List<String> def, @Nonnull Splitter splitter);

	@Nullable
	String getValue(@Nonnull String key, @Nullable String defVal, boolean defaultSpecified,
			@Nullable Config outerConfig, @Nullable Set<String> usedKeys);

	@Nonnull
	ConfigBuilder toBuilder();

	@Nonnull
	Set<String> keySet();

	@Nonnull
	default Config prefix(@Nonnull String prefix) {
		checkNotNull(prefix);
		return new PrefixConfig(this, prefix);
	}

	@Nonnull
	default Config subset(@Nonnull String prefix) {
		Config allowConfig = toBuilder().allowUndefined(true).build();
		ConfigBuilder builder = Configs.newBuilder();
		for (String key : getPrefixKeys(prefix)) {
			builder.setString(key.substring(prefix.length() + 1), allowConfig.getString(key));
		}
		return builder.build();
	}

	@Nonnull
	default Map<String, String> asMap() {
		Map<String, String> map = new LinkedHashMap<>();
		Config config = toBuilder().allowUndefined(true).build();
		keySet().forEach(key -> map.put(key, config.getString(key)));
		return ImmutableMap.copyOf(map);
	}

	@Nonnull
	default SortedMap<String, String> asSortedMap() {
		Config config = toBuilder().allowUndefined(true).build();
		return ImmutableSortedMap.copyOf(keySet().stream().collect(toMap(key -> key, config::getString)));
	}

	@Nonnull
	default Map<String, String> asRawMap() {
		return ImmutableMap.copyOf(keySet().stream().collect(toMap(key -> key, this::getRaw)));
	}

	@Nonnull
	default Properties asProperties() {
		Properties properties = new Properties();
		asMap().forEach(properties::put);
		return properties;
	}

	@Nonnull
	default Set<String> getPrefixKeys(@Nonnull String prefix) {
		checkNotNull(prefix);
		return keySet().stream().filter(key -> key.startsWith(prefix + ".")).collect(toSet());
	}

	@Nonnull
	default Optional<String> getMatch(@Nonnull String key, @Nonnull String regex) {
		checkNotNull(key);
		checkNotNull(regex);
		for (String item : getList(key, Configs.emptyList())) {
			Iterator<String> it = Splitter.on(':').split(item).iterator();
			if (regex.matches(it.next()) && it.hasNext()) {
				String result = it.next();
				return result.isEmpty() ? Optional.empty() : Optional.of(result);
			}
		}
		return Optional.empty();
	}
}
