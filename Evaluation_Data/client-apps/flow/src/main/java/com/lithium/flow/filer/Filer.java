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

import com.lithium.flow.io.DataIo;
import com.lithium.flow.util.BaseEncodings;
import com.lithium.flow.util.HashEncoder;
import com.lithium.flow.util.HashFunctions;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;

/**
 * A file system representation to get file records, read and write streams, and change attributes.
 *
 * @author Matt Ayres
 */
public interface Filer extends Closeable {
	@Nonnull
	URI getUri() throws IOException;

	@Nonnull
	List<Record> listRecords(@Nonnull String path) throws IOException;

	@Nonnull
	Record getRecord(@Nonnull String path) throws IOException;

	@Nonnull
	default Stream<Record> findRecords(@Nonnull String path, int threads) throws IOException {
		return RecordFinder.stream(this, path, threads);
	}

	@Nonnull
	default String getHash(@Nonnull String path, @Nonnull String hash, @Nonnull String base) throws IOException {
		return new HashEncoder(HashFunctions.of(hash), BaseEncodings.of(base)).process(readFile(path));
	}

	@Nonnull
	InputStream readFile(@Nonnull String path) throws IOException;

	@Nonnull
	OutputStream writeFile(@Nonnull String path) throws IOException;

	@Nonnull
	OutputStream appendFile(@Nonnull String path) throws IOException;

	@Nonnull
	DataIo openFile(@Nonnull String path, boolean write) throws IOException;

	void setFileTime(@Nonnull String path, long time) throws IOException;

	void deleteFile(@Nonnull String path) throws IOException;

	void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException;

	void createDirs(@Nonnull String path) throws IOException;

	void deleteDir(@Nonnull String path) throws IOException;

	default void createFolder(@Nonnull String path) throws IOException {
		createDirs(RecordPath.getFolder(path));
	}

	default void copy(@Nonnull String srcPath, @Nonnull Filer destFiler, @Nonnull String destPath) throws IOException {
		destFiler.createFolder(destPath);

		try (InputStream in = readFile(srcPath)) {
			try (OutputStream out = destFiler.writeFile(destPath)) {
				IOUtils.copy(in, out, 65536);
			}
		}
	}

	@Override
	void close() throws IOException;
}
