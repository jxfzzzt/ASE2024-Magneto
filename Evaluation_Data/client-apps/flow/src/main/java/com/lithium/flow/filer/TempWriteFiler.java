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

import com.lithium.flow.io.DecoratedOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class TempWriteFiler extends DecoratedFiler {
	private final AtomicInteger num = new AtomicInteger();
	private final String dirPath;
	private final String extension;
	private final boolean overwrite;

	public TempWriteFiler(@Nonnull Filer delegate, @Nonnull String dirPath, @Nonnull String extension) {
		this(delegate, dirPath, extension, false);
	}

	public TempWriteFiler(@Nonnull Filer delegate, @Nonnull String dirPath, @Nonnull String extension,
			boolean overwrite) {
		super(delegate);
		this.dirPath = checkNotNull(dirPath);
		this.extension = checkNotNull(extension);
		this.overwrite = overwrite;
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		String tempPath = getTempPath(path);
		OutputStream out = super.writeFile(tempPath);
		return new DecoratedOutputStream(out) {
			@Override
			public void close() throws IOException {
				super.close();
				if (getRecord(path).exists()) {
					if (overwrite) {
						deleteFile(path);
					} else {
						throw new IOException("destination already exists: " + path);
					}
				} else {
					createDirs(RecordPath.getFolder(path));
				}
				renameFile(tempPath, path);
			}
		};
	}

	@Nonnull
	private String getTempPath(@Nonnull String path) {
		String tempPath = dirPath + "/" + path.replace('/', '_') + '_' + num.incrementAndGet() + extension;
		if (tempPath.length() > 255) {
			tempPath = tempPath.substring(tempPath.length() - 255);
		}
		return tempPath;
	}
}
