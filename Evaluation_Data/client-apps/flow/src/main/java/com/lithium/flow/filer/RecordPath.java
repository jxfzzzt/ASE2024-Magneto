/*
 * Copyright 2017 Lithium Technologies, Inc.
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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Matt Ayres
 */
public class RecordPath implements Serializable {
	private static final long serialVersionUID = 3989547807576183269L;

	private final String path;
	private final String folder;
	private final String name;

	private RecordPath(@Nonnull String path, @Nonnull String folder, @Nonnull String name) {
		this.path = path;
		this.folder = folder;
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public String getFolder() {
		return folder;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		RecordPath that = (RecordPath) o;
		return Objects.equals(path, that.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path);
	}

	@Override
	@Nonnull
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Nonnull
	public static RecordPath from(@Nonnull String path) {
		checkNotNull(path);
		path = fix(path);

		String folder;
		String name;

		int index = path.lastIndexOf('/');
		if (index > -1) {
			folder = path.substring(0, index);
			name = path.substring(index + 1);
		} else {
			folder = "";
			name = path;
		}

		return new RecordPath(path, folder, name);
	}

	@Nonnull
	public static RecordPath from(@Nonnull String folder, @Nonnull String name) {
		checkNotNull(folder);
		checkNotNull(name);
		checkArgument(!name.contains("/"), "name cannot include '/' in it: %s", name);

		folder = fix(folder);
		String path = folder + "/" + name;

		return new RecordPath(path, folder, name);
	}

	@Nonnull
	public static String getFolder(@Nonnull String path) {
		return from(path).getFolder();
	}

	@Nonnull
	public static String getName(@Nonnull String path) {
		return from(path).getName();
	}

	@Nonnull
	private static String fix(@Nonnull String path) {
		// force slash as separator
		if (File.separatorChar != '/' && path.contains(File.separator)) {
			path = path.replace(File.separatorChar, '/');
		}

		// remove any trailing slashes
		while (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		return path;
	}
}
