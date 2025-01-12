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

import com.lithium.flow.config.exception.IllegalConfigException;
import com.lithium.flow.store.Store;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

/**
 * Base immutable implementation of {@link Config}.
 *
 * @author Matt Ayres
 */
public final class BaseConfig extends AbstractConfig {
	private final String name;
	private final Store store;
	private final Config defaults;
	private final boolean allowUndefined;

	public BaseConfig(@Nonnull String name, @Nonnull Store store) {
		this(name, store, Configs.empty(), false);
	}

	public BaseConfig(@Nonnull String name, @Nonnull Store store, @Nullable Config defaults, boolean allowUndefined) {
		this.name = checkNotNull(name);
		this.store = checkNotNull(store);
		this.defaults = defaults;
		this.allowUndefined = allowUndefined;
	}

	@Override
	@Nonnull
	public final String getName() {
		return name;
	}

	@Override
	public boolean isAllowUndefined() {
		return allowUndefined;
	}

	@Override
	public final boolean containsKey(@Nonnull String key) {
		checkNotNull(key);
		try {
			return getValue(key, null, true, null, null) != null;
		} catch (IllegalConfigException e) {
			return false;
		}
	}

	@Override
	@Nullable
	public final String getRaw(@Nonnull String key) {
		checkNotNull(key);
		String value = store.getValue(key);
		return value != null ? value : defaults != null ? defaults.getRaw(key) : null;
	}

	@Override
	@Nonnull
	public Set<String> keySet() {
		ImmutableSet.Builder<String> builder = ImmutableSet.builder();
		if (defaults != null) {
			builder.addAll(defaults.keySet());
		}
		builder.addAll(store.getKeys());
		return builder.build();
	}

	@Override
	@Nullable
	public final String getValue(@Nonnull String key, @Nullable String defaultValue, boolean defaultSpecified,
			@Nullable Config outerConfig, @Nullable Set<String> usedKeys) {
		checkNotNull(key);

		Config varConfig = outerConfig != null ? outerConfig : this;

		String value = store.getValue(key);
		if (value != null) {
			StringBuilder sb = new StringBuilder();

			int index = 0;
			while (index < value.length()) {
				int index1 = value.indexOf("${", index);
				if (index1 == -1) {
					break;
				}

				int index2 = value.indexOf("}", index1 + 2);
				if (index2 == -1) {
					break;
				}

				sb.append(value, index, index1);

				String subKey = value.substring(index1 + 2, index2);
				if (usedKeys == null) {
					usedKeys = new HashSet<>();
				}
				if (usedKeys.add(subKey)) {
					String subValue = varConfig.getValue(subKey, null, true, varConfig, usedKeys);
					usedKeys.remove(subKey);
					if (subValue != null) {
						sb.append(subValue);
					} else if (varConfig.isAllowUndefined()) {
						sb.append("${").append(subKey).append("}");
					} else {
						throw new IllegalConfigException(subKey);
					}
				} else {
					throw new IllegalConfigException(key, value, "recursive", null);
				}

				index = index2 + 1;
			}

			sb.append(value.substring(index));

			return sb.toString();
		} else if (defaults != null) {
			return defaults.getValue(key, defaultValue, defaultSpecified, varConfig, null);
		} else if (defaultSpecified || varConfig.isAllowUndefined()) {
			return defaultValue;
		} else {
			throw new IllegalConfigException(key);
		}
	}
}
