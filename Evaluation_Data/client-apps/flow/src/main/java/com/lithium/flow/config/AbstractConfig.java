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
import static java.util.stream.Collectors.toList;

import com.lithium.flow.config.exception.IllegalConfigException;
import com.lithium.flow.util.TimeUtils;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * @author Matt Ayres
 */
public abstract class AbstractConfig implements Config {
	private static final Splitter DEFAULT_SPLITTER = Splitter.on(LIST_SPLIT_PATTERN);

	protected AbstractConfig() {
	}

	@Override
	@Nullable
	public abstract String getValue(@Nonnull String key, @Nullable String defVal, boolean defaultSpecified,
			@Nullable Config outerConfig, @Nullable Set<String> usedKeys);

	private String getValue(@Nonnull String key, @Nullable String defaultValue, boolean defaultSpecified) {
		return getValue(key, defaultValue, defaultSpecified, null, null);
	}

	@Override
	public final String getString(@Nonnull String key) {
		return getValue(key, null, false);
	}

	@Override
	public final String getString(@Nonnull String key, @Nullable String def) {
		return getValue(key, def, true);
	}

	@Override
	public final int getInt(@Nonnull String key) {
		String value = getValue(key, "0", false);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new IllegalConfigException(key, value, "int", e);
		}
	}

	@Override
	public final int getInt(@Nonnull String key, int def) {
		return getValue(key, null, true) == null ? def : getInt(key);
	}

	@Override
	public final long getLong(@Nonnull String key) {
		String value = getValue(key, "0", false);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new IllegalConfigException(key, value, "long", e);
		}
	}

	@Override
	public final long getLong(@Nonnull String key, long def) {
		return getValue(key, null, true) == null ? def : getLong(key);
	}

	@Override
	public final long getTime(@Nonnull String key) {
		String value = getValue(key, "0", false);
		try {
			return TimeUtils.getMillisValue(value);
		} catch (NumberFormatException e) {
			throw new IllegalConfigException(key, value, "time", e);
		}
	}

	@Override
	public final long getTime(@Nonnull String key, String def) {
		return getValue(key, null, true) == null ? TimeUtils.getMillisValue(def) : getTime(key);
	}

	@Override
	public final double getDouble(@Nonnull String key) {
		String value = getValue(key, "0", false);
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new IllegalConfigException(key, value, "double", e);
		}
	}

	@Override
	public final double getDouble(@Nonnull String key, double def) {
		return getValue(key, null, true) == null ? def : getDouble(key);
	}

	@Override
	public final boolean getBoolean(@Nonnull String key) {
		String value = getValue(key, null, false);
		if (value == null || value.equalsIgnoreCase("false")) {
			return false;
		} else if (value.equalsIgnoreCase("true")) {
			return true;
		} else {
			throw new IllegalConfigException(key, value, "boolean", null);
		}
	}

	@Override
	public final boolean getBoolean(@Nonnull String key, boolean def) {
		return getValue(key, null, true) == null ? def : getBoolean(key);
	}

	@Override
	@Nonnull
	public final List<String> getList(@Nonnull String key) {
		List<String> list = getList(key, Configs.emptyList());
		if (list.isEmpty()) {
			throw new IllegalConfigException(key);
		}
		return list;
	}

	@Override
	@Nonnull
	public final List<String> getList(@Nonnull String key, @Nonnull List<String> def) {
		return getList(key, def, DEFAULT_SPLITTER);
	}

	@Override
	@Nonnull
	public List<String> getList(@Nonnull String key, @Nonnull Splitter splitter) {
		return getList(key, Configs.emptyList(), splitter);
	}

	@Override
	@Nonnull
	public final List<String> getList(@Nonnull String key, @Nonnull List<String> def,
			@Nonnull Splitter splitter) {
		checkNotNull(key);
		checkNotNull(def);
		checkNotNull(splitter);

		String value = getString(key, null);
		if (value != null) {
			Iterable<String> split = splitter.split(value);
			return Lists.newArrayList(split).stream().filter(item -> item.length() > 0).collect(toList());
		} else {
			return Lists.newArrayList(def);
		}
	}

	@Override
	@Nonnull
	public ConfigBuilder toBuilder() {
		return Configs.newBuilder(this).allowUndefined(isAllowUndefined()).setName(getName());
	}
}
