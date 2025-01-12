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
import com.lithium.flow.replacer.StringReplacer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * Decorates an instance of {@link Filer} to transparently renameFile files.
 *
 * @author Matt Ayres
 */
public class RenameFiler implements Filer {
	private final Filer delegate;
	private final StringReplacer toReplacer;
	private final StringReplacer fromReplacer;

	public RenameFiler(@Nonnull Filer delegate, @Nonnull StringReplacer toReplacer,
			@Nonnull StringReplacer fromReplacer) {
		this.delegate = checkNotNull(delegate);
		this.toReplacer = checkNotNull(toReplacer);
		this.fromReplacer = checkNotNull(fromReplacer);
	}

	@Override
	@Nonnull
	public java.net.URI getUri() throws IOException {
		return delegate.getUri();
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		path = toReplacer.replace(path);
		return adjustRecord(delegate.getRecord(path));
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		path = toReplacer.replace(path);
		List<Record> records = new ArrayList<>();
		for (Record record : delegate.listRecords(path)) {
			records.add(adjustRecord(record));
		}
		return records;
	}

	@Nonnull
	@Override
	public Stream<Record> findRecords(@Nonnull String path, int threads) throws IOException {
		return delegate.findRecords(path, threads).map(this::adjustRecord);
	}

	@Nonnull
	private Record adjustRecord(@Nonnull Record record) {
		return record.withPath(fromReplacer.replace(record.getPath()));
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		path = toReplacer.replace(path);
		return delegate.readFile(path);
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		path = toReplacer.replace(path);
		return delegate.writeFile(path);
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		path = toReplacer.replace(path);
		return delegate.appendFile(path);
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) throws IOException {
		path = toReplacer.replace(path);
		return delegate.openFile(path, write);
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		path = toReplacer.replace(path);
		delegate.setFileTime(path, time);
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		path = toReplacer.replace(path);
		delegate.deleteFile(path);
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		delegate.createDirs(path);
	}

	@Override
	public void deleteDir(@Nonnull String path) throws IOException {
		path = toReplacer.replace(path);
		delegate.deleteDir(path);
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		oldPath = toReplacer.replace(oldPath);
		newPath = toReplacer.replace(newPath);
		delegate.renameFile(oldPath, newPath);
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}
}
