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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Decorates an instance of {@link Filer} to prevent writes.
 *
 * @author Matt Ayres
 */
public class ReadOnlyFiler extends DecoratedFiler {
	private final Predicate<Filer> predicate;

	public ReadOnlyFiler(@Nonnull Filer delegate) {
		this(delegate, filer -> true);
	}

	public ReadOnlyFiler(@Nonnull Filer delegate, @Nonnull Predicate<Filer> predicate) {
		super(checkNotNull(delegate));
		this.predicate = checkNotNull(predicate);
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		if (predicate.test(this)) {
			throw new IOException("read only: " + path);
		} else {
			return super.writeFile(path);
		}
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		if (predicate.test(this)) {
			throw new IOException("read only: " + path);
		} else {
			return super.appendFile(path);
		}
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		if (predicate.test(this)) {
			throw new IOException("read only: " + path);
		} else {
			super.setFileTime(path, time);
		}
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		if (predicate.test(this)) {
			throw new IOException("read only: " + path);
		} else {
			super.deleteFile(path);
		}
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		if (predicate.test(this)) {
			throw new IOException("read only: " + path);
		} else {
			super.createDirs(path);
		}
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		if (predicate.test(this)) {
			throw new IOException("read only: " + oldPath);
		} else {
			super.renameFile(oldPath, newPath);
		}
	}
}
