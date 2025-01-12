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

import com.lithium.flow.io.AbstractDataIo;
import com.lithium.flow.io.DataIo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Local file system implementation of {@link Filer}.
 *
 * @author Matt Ayres
 */
public class LocalFiler implements Filer {
	@Override
	@Nonnull
	public URI getUri() {
		return new File("/").toURI();
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		checkNotNull(path);

		List<Record> records = new ArrayList<>();

		File parentFile = new File(path);
		File[] files = parentFile.listFiles();
		if (files != null) {
			for (File file : files) {
				String name = file.getName();
				long time = file.lastModified();
				long size = file.length();
				records.add(new Record(getUri(), RecordPath.from(parentFile.getCanonicalPath(), name), time,
						size, file.isDirectory()));
			}
		}

		return records;
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) {
		checkNotNull(path);

		File file = new File(path);
		if (file.exists()) {
			return new Record(getUri(), RecordPath.from(path), file.lastModified(), file.length(), file.isDirectory());
		} else {
			return new Record(getUri(), RecordPath.from(path), 0, Record.NO_EXIST_SIZE, false);
		}
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		checkNotNull(path);
		File file = new File(path);
		if (file.exists()) {
			return new FileInputStream(new File(path));
		} else {
			throw new FileNotFoundException(path);
		}
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		checkNotNull(path);
		return new FileOutputStream(path);
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		checkNotNull(path);
		return new FileOutputStream(path, true);
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) throws IOException {
		checkNotNull(path);
		RandomAccessFile file = new RandomAccessFile(path, write ? "rw" : "r");
		return new AbstractDataIo(file, file) {
			@Override
			public long getFilePointer() throws IOException {
				return file.getFilePointer();
			}

			@Override
			public void seek(long pos) throws IOException {
				file.seek(pos);
			}

			@Override
			public long length() throws IOException {
				return file.length();
			}

			@Override
			public void close() throws IOException {
				file.close();
			}
		};
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		File file = new File(path);
		if (!file.setLastModified(time)) {
			throw new IOException("failed to set time: " + path);
		}
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		checkNotNull(path);
		if (!new File(path).delete()) {
			throw new IOException("failed to delete: " + path);
		}
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		checkNotNull(path);
		File file = new File(path);
		if (!file.exists() && !file.mkdirs()) {
			throw new IOException("failed to create dirs: " + path);
		}
	}

	@Override
	public void deleteDir(@Nonnull String path) throws IOException {
		deleteFile(path);
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		File oldFile = new File(checkNotNull(oldPath));
		File newFile = new File(checkNotNull(newPath));
		createDirs(newFile.getParentFile().getAbsolutePath());

		Files.move(oldFile.toPath(), newFile.toPath());
	}

	@Override
	public void close() {
	}
}
