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
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * Decorates an instance of {@link Filer} to filter available records by specified predicate.
 *
 * @author Matt Ayres
 */
public class FilteredFiler extends DecoratedFiler {
	private final Predicate<Record> predicate;

	public FilteredFiler(@Nonnull Filer delegate, @Nonnull Predicate<Record> predicate) {
		super(delegate);
		bypassDelegateFind = true;

		this.predicate = checkNotNull(predicate);
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		return super.listRecords(path).stream().filter(predicate).collect(toList());
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		Record record = super.getRecord(path);
		if (predicate.test(record)) {
			return record;
		} else {
			return record.withSize(Record.NO_EXIST_SIZE);
		}
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		Record record = super.getRecord(path);
		if (predicate.test(record)) {
			return super.readFile(path);
		} else {
			throw new IOException("path is filtered: " + path);
		}
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		Record record = super.getRecord(path);
		if (predicate.test(record)) {
			return super.writeFile(path);
		} else {
			throw new IOException("path is filtered: " + path);
		}
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		Record record = super.getRecord(path);
		if (predicate.test(record)) {
			super.setFileTime(path, time);
		}
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		Record record = super.getRecord(path);
		if (predicate.test(record)) {
			super.deleteFile(path);
		}
	}
}
