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

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.ConfigLoader;
import com.lithium.flow.config.Configs;
import com.lithium.flow.config.loaders.ClasspathConfigLoader;
import com.lithium.flow.config.loaders.FileConfigLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public interface Main {
	Logger log = Logs.getLogger(3);

	void main(@Nonnull Config config) throws Exception;

	static void run() {
		run(Thread.currentThread().getStackTrace()[2].getClassName());
	}

	static void run(@Nonnull String className) {
		try {
			run(Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	static <T> T run(@Nonnull Class<T> clazz) {
		checkNotNull(clazz);
		AtomicReference<T> ref = new AtomicReference<>();
		run(clazz, config -> ref.set(run(clazz, config)));
		return ref.get();
	}

	@Nonnull
	static <T> T run(@Nonnull Class<T> clazz, @Nonnull Config config) throws Exception {
		T instance;
		try {
			instance = clazz.getDeclaredConstructor(Config.class).newInstance(config);
		} catch (NoSuchMethodException e) {
			instance = clazz.getDeclaredConstructor().newInstance();
		}

		if (instance instanceof Main) {
			((Main) instance).main(config);
		}

		return instance;
	}

	static void run(@Nonnull CheckedConsumer<Config, Exception> callback) {
		run(null, callback);
	}

	static void run(@Nullable Class<?> clazz, @Nonnull CheckedConsumer<Config, Exception> callback) {
		checkNotNull(callback);
		try {
			Config config = config(clazz);

			Map<String, String> map = new HashMap<>();
			for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
				map.put(entry.getKey().toString(), entry.getValue().toString());
			}
			config = config.toBuilder().setAll(map).build();

			Logs.configure(config);
			Logs.redirect(config);
			callback.accept(config);
		} catch (Exception e) {
			log.error("failed to start", e);
			System.exit(1);
		}
	}

	@Nonnull
	static Config config() throws IOException {
		return config(null);
	}

	@Nonnull
	static Config config(@Nullable Class<?> clazz) throws IOException {
		String path = System.getProperty("config");
		if (path != null) {
			return configFromFile(path);
		}

		// legacy support
		path = System.getProperty("local.config");
		if (path != null) {
			return configFromFile(path);
		}

		// attempt to find 'local.config' file
		File file = new File("local.config");
		if (file.exists()) {
			return configFromFile(file.getAbsolutePath());
		}

		// use class resource if possible
		if (clazz != null) {
			ConfigLoader loader = new ClasspathConfigLoader();
			path = "/" + clazz.getSimpleName() + ".config";
			try (InputStream in = loader.getInputStream(path)) {
				if (in != null) {
					log.info("using config resource: {}", path);
					return Configs.newBuilder().addLoader(loader).include(path).build();
				}
			}
		}

		log.info("using empty config");
		return Configs.empty();
	}

	@Nonnull
	static Config configFromFile(@Nonnull String path) throws IOException {
		checkNotNull(path);

		File file = new File(path);
		if (!file.exists()) {
			throw new IOException("config file not found: " + path);
		}

		log.info("using config file: {}", path);

		// this loader allows for includes relative to the local.config parent path
		ConfigLoader loader = new FileConfigLoader(file.getParent());

		return Configs.newBuilder().addLoader(loader).include(path).build();
	}
}
