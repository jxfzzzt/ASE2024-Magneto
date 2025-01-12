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

package com.lithium.flow.table;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public interface Table extends Closeable {
	@Nullable
	default <T> T getCell(@Nonnull Key key, @Nonnull String column, @Nonnull Class<T> clazz) {
		checkNotNull(key);
		checkNotNull(column);
		checkNotNull(clazz);
		return getRow(key).getCell(column, clazz);
	}

	default <T> void putCell(@Nonnull Key key, @Nonnull String column, @Nullable T value) {
		checkNotNull(key);
		checkNotNull(column);
		getRow(key).putCell(column, value);
	}

	@Nonnull
	Row getRow(@Nonnull Key key);

	void putRow(@Nonnull Row row);

	default void updateRow(@Nonnull Row row) {
		Key key = row.getKey();
		Row newRow = new Row(key).putAll(getRow(key)).putAll(row);
		putRow(newRow);
	}

	void deleteRow(@Nonnull Key key);

	default void putRows(@Nonnull List<Row> rows) {
		rows.forEach(this::putRow);
	}

	@Nonnull
	default Stream<Key> keys() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	default Stream<Row> rows() {
		throw new UnsupportedOperationException();
	}

	default long copyTo(@Nonnull Table table) {
		checkNotNull(table);
		return rows().peek(table::putRow).count();
	}

	default void close() throws IOException {
	}
}
