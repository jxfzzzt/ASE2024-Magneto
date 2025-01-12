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

import com.lithium.flow.io.DataIo;
import com.lithium.flow.io.DecoratedOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.AccessControlException;

/**
 * @author Matt Ayres
 */
public class HdfsFiler implements Filer {
	private final FileSystem fileSystem;
	private final boolean overwrite;
	private final boolean hflush;
	private final boolean hsync;

	public HdfsFiler(@Nonnull Configuration conf) throws IOException {
		this(FileSystem.get(checkNotNull(conf)));
	}

	public HdfsFiler(@Nonnull FileSystem fileSystem) {
		this.fileSystem = checkNotNull(fileSystem);
		overwrite = fileSystem.getConf().getBoolean("overwrite", true);
		hflush = fileSystem.getConf().getBoolean("hflush", false);
		hsync = fileSystem.getConf().getBoolean("hsync", true);
	}

	@Override
	@Nonnull
	public URI getUri() {
		return fileSystem.getUri();
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		List<Record> records = new ArrayList<>();

		Path hdfsPath = new Path(path);
		if (fileSystem.exists(hdfsPath)) {
			try {
				for (FileStatus status : fileSystem.listStatus(hdfsPath)) {
					records.add(getRecordForStatus(status, path));
				}
			} catch (AccessControlException e) {
				// permission denied, fall through to return the empty list
			}
		}

		return records;
	}

	@Nonnull
	@Override
	public Record getRecord(@Nonnull String path) throws IOException {
		Path filePath = new Path(path);
		try {
			FileStatus status = fileSystem.getFileStatus(filePath);
			if (status != null) {
				return getRecordForStatus(status, RecordPath.getFolder(path));
			}
		} catch (FileNotFoundException e) {
			// catch this here to avoid calling fileSystem.exists() that does the same thing
		}
		return new Record(getUri(), RecordPath.from(filePath.getParent().toString(), filePath.getName()), 0, -1, false);
	}

	private Record getRecordForStatus(@Nonnull FileStatus status, @Nonnull String parent) {
		String name = status.getPath().getName();
		long time = status.getModificationTime();
		long size = status.getLen();
		boolean directory = status.isDirectory();
		return new Record(getUri(), RecordPath.from(parent, name), time, size, directory);
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		checkNotNull(path);

		return fileSystem.open(new Path(path));
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		checkNotNull(path);
		return wrapOut(fileSystem.create(new Path(path), overwrite));
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		checkNotNull(path);
		Path fsPath = new Path(path);
		fileSystem.createNewFile(fsPath);
		return wrapOut(fileSystem.append(fsPath));
	}

	@Nonnull
	private OutputStream wrapOut(@Nonnull FSDataOutputStream fsOut) {
		return new DecoratedOutputStream(fsOut) {
			@Override
			public void flush() throws IOException {
				out.flush();
				if (hflush) {
					fsOut.hflush();
				}
				if (hsync) {
					fsOut.hsync();
				}
			}

			@Override
			public void close() throws IOException {
				try {
					flush();
				} finally {
					out.close();
				}
			}
		};
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) {
		throw new UnsupportedOperationException("openFile not implemented yet");
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		checkNotNull(path);

		FileStatus status = fileSystem.getFileStatus(new Path(path));
		if (status != null) {
			fileSystem.setTimes(new Path(path), time, status.getAccessTime());
		}
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		checkNotNull(path);

		fileSystem.delete(new Path(path), false);
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		checkNotNull(path);

		if (!fileSystem.mkdirs(new Path(path))) {
			throw new IOException("failed to create dirs: " + path);
		}
	}

	@Override
	public void deleteDir(@Nonnull String path) throws IOException {
		deleteFile(path);
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		checkNotNull(oldPath);
		checkNotNull(newPath);

		if (!fileSystem.rename(new Path(oldPath), new Path(newPath))) {
			throw new IOException("failed to rename: " + oldPath + " to " + newPath);
		}
	}

	@Override
	public void close() throws IOException {
		fileSystem.close();
	}
}
