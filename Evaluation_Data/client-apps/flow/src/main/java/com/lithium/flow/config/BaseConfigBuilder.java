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
import com.lithium.flow.config.loaders.ClasspathConfigLoader;
import com.lithium.flow.config.loaders.FileConfigLoader;
import com.lithium.flow.config.parsers.AppendConfigParser;
import com.lithium.flow.config.parsers.CommentConfigParser;
import com.lithium.flow.config.parsers.EqualsConfigParser;
import com.lithium.flow.config.parsers.IncludeConfigParser;
import com.lithium.flow.config.parsers.LoaderConfigParser;
import com.lithium.flow.config.parsers.RequiredConfigParser;
import com.lithium.flow.config.parsers.SetNullConfigParser;
import com.lithium.flow.config.parsers.SubtractConfigParser;
import com.lithium.flow.store.MemoryStore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public class BaseConfigBuilder implements ConfigBuilder {
	private final Map<String, String> rawMap = new LinkedHashMap<>();
	private final Set<String> requiredKeys = new HashSet<>();
	private final Deque<String> pathDeque = new ArrayDeque<>();
	private final Deque<String> prefixDeque = new ArrayDeque<>();
	private final List<ConfigLoader> loaders = new ArrayList<>();
	private final List<ConfigParser> parsers = new ArrayList<>();
	private final List<ConfigWatcher> watchers = new ArrayList<>();
	private String name = "unknown";
	private boolean allowFileNotFound;
	private boolean allowUndefined;
	private boolean allowRequiredKeys;
	private final Config defaults;

	public BaseConfigBuilder() {
		this(null);
	}

	public BaseConfigBuilder(@Nullable Config defaults) {
		this.defaults = defaults;

		loaders.add(new FileConfigLoader());
		loaders.add(new ClasspathConfigLoader());

		parsers.add(new CommentConfigParser());
		parsers.add(new IncludeConfigParser());
		parsers.add(new LoaderConfigParser(this::addLoader));

		parsers.add(new AppendConfigParser());
		parsers.add(new SubtractConfigParser());
		parsers.add(new SetNullConfigParser());
		parsers.add(new RequiredConfigParser());
		parsers.add(new EqualsConfigParser());
	}

	@Override
	@Nonnull
	public ConfigBuilder pushPrefix(@Nonnull String prefix) {
		prefixDeque.push(checkNotNull(prefix));
		return this;
	}

	@Override
	@Nonnull
	public ConfigBuilder popPrefix() {
		prefixDeque.pop();
		return this;
	}

	@Nonnull
	private String getPrefixed(@Nonnull String key) {
		StringBuilder sb = new StringBuilder();
		prefixDeque.forEach(prefix -> sb.append(prefix).append('.'));
		sb.append(key);
		return sb.toString();
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder addLoader(@Nonnull ConfigLoader loader) {
		loaders.add(checkNotNull(loader));
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder resetLoaders() {
		loaders.clear();
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder addParser(@Nonnull ConfigParser parser) {
		parsers.add(checkNotNull(parser));
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder resetParsers() {
		parsers.clear();
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder addWatcher(@Nonnull ConfigWatcher watcher) {
		watchers.add(checkNotNull(watcher));
		watcher.onStart(this);
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder allowFileNotFound(boolean allowFileNotFound) {
		this.allowFileNotFound = allowFileNotFound;
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder allowUndefined(boolean allowUndefined) {
		this.allowUndefined = allowUndefined;
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder allowRequiredKeys(boolean allowRequiredKeys) {
		this.allowRequiredKeys = allowRequiredKeys;
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder setName(@Nonnull String name) {
		this.name = checkNotNull(name);
		return this;
	}

	@Override
	@Nonnull
	public final Config build() {
		Config config = new BaseConfig(name, new MemoryStore(rawMap), defaults, allowUndefined);
		if (allowRequiredKeys) {
			for (String key : requiredKeys) {
				if (config.getString(key, "").isEmpty()) {
					throw new IllegalConfigException(key);
				}
			}
		}
		return config;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder setString(@Nonnull String key, @Nonnull String value) {
		checkNotNull(key);
		checkNotNull(value);
		String prefixedKey = getPrefixed(key);
		rawMap.put(prefixedKey, value);
		watchers.forEach(watcher -> watcher.onSet(prefixedKey, value));
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder setAll(@Nonnull Map<String, String> configValues) {
		configValues.forEach(this::setString);
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder addAll(@Nonnull Config config) {
		setAll(config.asRawMap());
		return this;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder removeKey(@Nonnull String key) {
		checkNotNull(key);
		key = getPrefixed(key);
		rawMap.remove(key);
		return this;
	}

	@Nonnull
	@Override
	public final BaseConfigBuilder requireKey(@Nonnull String key) {
		requiredKeys.add(checkNotNull(key));
		return this;
	}

	@Override
	public boolean containsKey(@Nonnull String key) {
		checkNotNull(key);
		key = getPrefixed(key);
		return rawMap.containsKey(key) || (defaults != null && defaults.containsKey(key));
	}

	@Override
	@Nullable
	public final String getString(@Nonnull String key) {
		checkNotNull(key);
		key = getPrefixed(key);
		String value = rawMap.get(key);
		return value != null ? value : defaults != null ? defaults.getString(key) : null;
	}

	@Override
	@Nonnull
	public final BaseConfigBuilder include(@Nonnull String path) throws IOException {
		if (pathDeque.contains(checkNotNull(path))) {
			throw new IOException("include recursion detected: " + path);
		}

		InputStream in = getInputStream(path);
		if (in != null) {
			pathDeque.push(path);
			watchers.forEach(watcher -> watcher.onEnter(path));

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					parseLine(line);
				}
			}

			pathDeque.pop();
			watchers.forEach(watcher -> watcher.onExit(path));
		}

		return this;
	}

	@Nonnull
	public final BaseConfigBuilder parseLine(@Nonnull String line) throws IOException {
		for (ConfigParser parser : parsers) {
			if (parser.parseLine(line, this)) {
				break;
			}
		}
		return this;
	}

	@Nullable
	private InputStream getInputStream(@Nonnull String path) throws IOException {
		for (ConfigLoader loader : loaders) {
			InputStream in = loader.getInputStream(path);
			if (in != null) {
				return in;
			}
		}

		if (allowFileNotFound) {
			return null;
		} else {
			throw new FileNotFoundException(path);
		}
	}
}
