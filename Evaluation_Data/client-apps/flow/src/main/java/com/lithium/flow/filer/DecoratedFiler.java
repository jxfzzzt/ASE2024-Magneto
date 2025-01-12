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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * Base implementation of {@link Filer} for decoration.
 *
 * @author Matt Ayres
 */
public class DecoratedFiler implements Filer {
	private final Filer delegate;
	protected boolean bypassDelegateFind;
	protected boolean bypassDelegateHash;
	protected boolean bypassDelegateCopy;

	public DecoratedFiler(@Nonnull Filer delegate) {
		this.delegate = checkNotNull(delegate);
	}

	@Override
	@Nonnull
	public URI getUri() throws IOException {
		return delegate.getUri();
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		return delegate.listRecords(path);
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		return delegate.getRecord(path);
	}

	@Override
	@Nonnull
	public Stream<Record> findRecords(@Nonnull String path, int threads) throws IOException {
		if (bypassDelegateFind) {
			return Filer.super.findRecords(path, threads);
		} else {
			return delegate.findRecords(path, threads);
		}
	}

	@Override
	@Nonnull
	public String getHash(@Nonnull String path, @Nonnull String hash, @Nonnull String base) throws IOException {
		if (bypassDelegateHash) {
			return Filer.super.getHash(path, hash, base);
		} else {
			return delegate.getHash(path, hash, base);
		}
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		return delegate.readFile(path);
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		return delegate.writeFile(path);
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		return delegate.appendFile(path);
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) throws IOException {
		return delegate.openFile(path, write);
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		delegate.setFileTime(path, time);
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		delegate.deleteFile(path);
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		delegate.createDirs(path);
	}

	@Override
	public void deleteDir(@Nonnull String path) throws IOException {
		delegate.deleteDir(path);
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		delegate.renameFile(oldPath, newPath);
	}

	@Override
	public void copy(@Nonnull String srcPath, @Nonnull Filer destFiler, @Nonnull String destPath) throws IOException {
		if (bypassDelegateCopy) {
			Filer.super.copy(srcPath, destFiler, destPath);
		} else {
			delegate.copy(srcPath, destFiler, destPath);
		}
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}
}
