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

package com.lithium.flow.config.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.ConfigBuilder;
import com.lithium.flow.config.Configs;
import com.lithium.flow.config.Repo;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Record;
import com.lithium.flow.io.Swallower;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class FilerRepo implements Repo {
	private final List<Filer> filers;
	private final List<String> paths;
	private final String extension;
	private final UnaryOperator<Config> operator;
	private final Supplier<ConfigBuilder> supplier;

	public FilerRepo(@Nonnull List<Filer> filers, @Nonnull List<String> paths) {
		this(filers, paths, ".config");
	}

	public FilerRepo(@Nonnull List<Filer> filers, @Nonnull List<String> paths, @Nonnull String extension) {
		this(filers, paths, extension, config -> config, Configs::newBuilder);
	}

	public FilerRepo(@Nonnull List<Filer> filers, @Nonnull List<String> paths, @Nonnull String extension,
			@Nonnull UnaryOperator<Config> operator, @Nonnull Supplier<ConfigBuilder> supplier) {
		this.filers = checkNotNull(filers);
		this.paths = checkNotNull(paths);
		this.extension = checkNotNull(extension);
		this.operator = checkNotNull(operator);
		this.supplier = checkNotNull(supplier);
	}

	@Override
	@Nonnull
	public List<String> getNames() throws IOException {
		List<String> names = new ArrayList<>();
		for (Filer filer : filers) {
			for (String path : paths) {
				for (Record record : filer.listRecords(path)) {
					String name = record.getName();
					if (!record.isDir() && !name.startsWith(".") && name.endsWith(extension)) {
						names.add(name.replaceFirst(extension + "$", ""));
					}
				}
			}
		}
		return names;
	}

	@Override
	@Nonnull
	public Config getConfig(@Nonnull String name) throws IOException {
		Set<String> fullPaths = new HashSet<>();

		String fullPath = null;
		Record record = null;

		find:
		for (Filer filer : filers) {
			if (paths.size() > 0) {
				for (String path : paths) {
					fullPath = path + "/" + name + extension;
					fullPaths.add(fullPath);
					record = filer.getRecord(fullPath);
					if (record.exists()) {
						break find;
					}
				}
			} else {
				fullPath = name + extension;
				fullPaths.add(fullPath);
				record = filer.getRecord(fullPath);
				if (record.exists()) {
					break;
				}
			}
		}

		if (record == null || !record.exists()) {
			throw new FileNotFoundException("no config entry: " + fullPaths);
		}

		ConfigBuilder builder = supplier.get();
		for (Filer filer : filers) {
			builder.addLoader(path -> {
				Record pathRecord = filer.getRecord(path);
				return pathRecord.exists() && pathRecord.isFile() ? filer.readFile(path) : null;
			});
		}
		builder.allowFileNotFound(true);
		builder.include(fullPath);
		builder.setName(name);

		return operator.apply(builder.build());
	}

	@Override
	public void close() {
		filers.forEach(Swallower::close);
	}
}
