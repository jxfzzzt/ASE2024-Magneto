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

package com.lithium.flow.store;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public class MemoryStore implements Store {
	private final Map<String, String> map;

	public MemoryStore() {
		this(Collections.synchronizedMap(new LinkedHashMap<>()));
	}

	public MemoryStore(@Nonnull Map<String, String> map) {
		this.map = checkNotNull(map);
	}

	public MemoryStore(@Nonnull Store store) {
		this();
		checkNotNull(store);
		for (String key : store.getKeys()) {
			String value = store.getValue(key);
			if (value != null) {
				putValue(key, value);
			}
		}
	}

	@Override
	public void putValue(@Nonnull String key, @Nullable String value) {
		if (value != null) {
			map.put(key, value);
		} else {
			map.remove(key);
		}
	}

	@Nullable
	@Override
	public String getValue(@Nonnull String key) {
		return map.get(key);
	}

	@Override
	@Nonnull
	public Set<String> getKeys() {
		return map.keySet();
	}
}
