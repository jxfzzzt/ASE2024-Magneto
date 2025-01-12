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

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Matt Ayres
 */
public class FileStore implements Store {
	private final File file;
	private final ObjectMapper mapper = new ObjectMapper().enable(INDENT_OUTPUT);

	public FileStore(@Nonnull File file) {
		this.file = checkNotNull(file);
	}

	@Override
	public void putValue(@Nonnull String key, @Nullable String value) {
		Map<String, String> map = read();
		if (value != null) {
			map.put(key, value);
		} else {
			map.remove(key);
		}
		write(map);
	}

	@Override
	@Nullable
	public String getValue(@Nonnull String key) {
		return read().get(key);
	}

	@Override
	@Nonnull
	public Set<String> getKeys() {
		return read().keySet();
	}

	@SuppressWarnings("unchecked")
	private synchronized Map<String, String> read() {
		if (file.exists()) {
			try {
				return mapper.readValue(file, Map.class);
			} catch (IOException e) {
				throw new StoreException("failed to read " + file, e);
			}
		} else {
			return new HashMap<>();
		}
	}

	private synchronized void write(@Nonnull Map<String, String> map) {
		try {
			mapper.writeValue(file, map);
		} catch (IOException e) {
			throw new StoreException("failed to write " + file, e);
		}
	}
}
