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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

/**
 * @author Matt Ayres
 */
public class PrefixConfig extends AbstractConfig {
	private final Config delegate;
	private final String prefix;

	public PrefixConfig(@Nonnull Config delegate, @Nonnull String prefix) {
		this.delegate = checkNotNull(delegate);
		this.prefix = checkNotNull(prefix) + ".";
	}

	@Nonnull
	private String prefixKey(@Nonnull String key) {
		return prefix + key;
	}

	@Override
	@Nonnull
	public String getName() {
		return delegate.getName();
	}

	@Override
	public boolean isAllowUndefined() {
		return delegate.isAllowUndefined();
	}

	@Override
	public boolean containsKey(@Nonnull String key) {
		return delegate.containsKey(prefixKey(key)) || delegate.containsKey(key);
	}

	@Override
	@Nullable
	public String getRaw(@Nonnull String key) {
		checkNotNull(key);
		String prefixKey = prefixKey(key);
		String raw = delegate.getRaw(prefixKey);
		return raw == null ? delegate.getRaw(key) : raw;
	}

	@Override
	public String getValue(@Nonnull String key, @Nullable String defVal, boolean defaultSpecified,
			@Nullable Config outerConfig, @Nullable Set<String> usedKeys) {
		String prefixKey = prefixKey(key);

		// Config used to supply variable substitutions to inner config
		Config varConfig = (outerConfig != null) ? outerConfig : this;

		String retVal = delegate.getValue(prefixKey, null, true, varConfig, usedKeys);
		if (retVal == null) {
			retVal = delegate.getValue(key, defVal, defaultSpecified, varConfig, usedKeys);
		}
		return retVal;
	}

	@Override
	@Nonnull
	public Set<String> keySet() {
		return asRawMap().keySet();
	}

	@Override
	@Nonnull
	public Map<String, String> asRawMap() {
		Map<String, String> rawMap = Maps.newLinkedHashMap();
		Map<String, String> baseRawMap = delegate.asRawMap();
		rawMap.putAll(baseRawMap);

		for (Map.Entry<String, String> configEntries : baseRawMap.entrySet()) {
			String key = configEntries.getKey();
			String value = configEntries.getValue();

			if (key.startsWith(prefix) && !key.equals(prefix)) {
				rawMap.put(key.substring(prefix.length()), value);
			}
		}

		return Collections.unmodifiableMap(rawMap);
	}
}
