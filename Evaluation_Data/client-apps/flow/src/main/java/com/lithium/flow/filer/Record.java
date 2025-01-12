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

import java.io.Serializable;
import java.net.URI;
import java.util.Comparator;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Details for a single file from a {@link Filer}.
 *
 * @author Matt Ayres
 */
public class Record implements Serializable {
	private static final long serialVersionUID = -4759712231322406072L;

	public static final long NO_EXIST_SIZE = -1;

	private final URI uri;
	private final RecordPath path;
	private final long time;
	private final long size;
	private final boolean dir;

	public Record(@Nonnull URI uri, @Nonnull RecordPath path, long time, long size, boolean dir) {
		this.uri = checkNotNull(uri);
		this.path = checkNotNull(path);
		this.time = time;
		this.size = size;
		this.dir = dir;
	}

	@Nonnull
	public URI getUri() {
		return uri;
	}

	@Nonnull
	public String getFolder() {
		return path.getFolder();
	}

	@Nonnull
	public String getName() {
		return path.getName();
	}

	@Nonnull
	public String getPath() {
		return path.getPath();
	}

	public long getTime() {
		return time;
	}

	public long getSize() {
		return size;
	}

	public boolean exists() {
		return size != NO_EXIST_SIZE;
	}

	public boolean isDir() {
		return dir;
	}

	public boolean isFile() {
		return !dir;
	}

	@Nonnull
	public Record withPath(@Nonnull String path) {
		return new Record(uri, RecordPath.from(path), time, size, dir);
	}

	@Nonnull
	public Record withSize(long newSize) {
		return new Record(uri, path, time, newSize, dir);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Record record = (Record) o;
		return time == record.time
				&& size == record.size
				&& dir == record.dir
				&& Objects.equals(uri, record.uri)
				&& Objects.equals(path, record.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uri, path, time, size, dir);
	}

	@Override
	@Nonnull
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Nonnull
	public static Record noFile(@Nonnull URI uri, @Nonnull String path) {
		checkNotNull(uri);
		checkNotNull(path);
		return new Record(uri, RecordPath.from(path), 0, NO_EXIST_SIZE, false);
	}

	@Nonnull
	public static Comparator<Record> sizeAsc() {
		return Comparator.comparingLong(Record::getSize);
	}

	@Nonnull
	public static Comparator<Record> sizeDesc() {
		return Comparator.comparingLong(Record::getSize).reversed();
	}

	@Nonnull
	public static Comparator<Record> timeAsc() {
		return Comparator.comparingLong(Record::getTime);
	}

	@Nonnull
	public static Comparator<Record> timeDesc() {
		return Comparator.comparingLong(Record::getTime).reversed();
	}

	@Nonnull
	public static Comparator<Record> nameAsc() {
		return Comparator.comparing(Record::getName);
	}

	@Nonnull
	public static Comparator<Record> nameDesc() {
		return Comparator.comparing(Record::getName).reversed();
	}

	@Nonnull
	public static Comparator<Record> pathAsc() {
		return Comparator.comparing(Record::getPath);
	}

	@Nonnull
	public static Comparator<Record> pathDesc() {
		return Comparator.comparing(Record::getPath).reversed();
	}
}
