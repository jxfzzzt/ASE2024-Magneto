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
import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Matt Ayres
 */
public class Row implements Iterable<Object> {
	private final Map<String, Object> map = Maps.newLinkedHashMap();
	private final Key key;

	public Row(@Nonnull Key key) {
		this.key = checkNotNull(key);
	}

	@Nonnull
	public Key getKey() {
		return key;
	}

	@Nonnull
	public Row withKey(@Nonnull Key withKey) {
		Row row = new Row(withKey);
		row.map.putAll(map);
		return row;
	}

	@Nullable
	public <T> T getCell(@Nonnull String column, @Nonnull Class<T> clazz) {
		checkNotNull(column);
		checkNotNull(clazz);

		Object value = map.get(column);
		if (value == null) {
			return null;
		} else if (clazz.isInstance(value)) {
			return clazz.cast(value);
		} else if (clazz.isAssignableFrom(String.class)) {
			return clazz.cast(value.toString());
		} else if (clazz.isAssignableFrom(Integer.class)) {
			return clazz.cast(Integer.parseInt(value.toString()));
		} else if (clazz.isAssignableFrom(Long.class)) {
			return clazz.cast(Long.parseLong(value.toString()));
		} else if (clazz.isAssignableFrom(Float.class)) {
			return clazz.cast(Float.parseFloat(value.toString()));
		} else if (clazz.isAssignableFrom(Double.class)) {
			return clazz.cast(Double.parseDouble(value.toString()));
		} else if (clazz.isAssignableFrom(Boolean.class)) {
			return clazz.cast(Boolean.parseBoolean(value.toString()));
		} else {
			throw new ClassCastException();
		}
	}

	@Nonnull
	public <T> Row putCell(@Nonnull String column, @Nullable T value) {
		checkNotNull(column);
		map.put(column, value);
		return this;
	}

	@Nonnull
	public Row putAll(@Nonnull Row row) {
		checkNotNull(row);
		map.putAll(row.map);
		return this;
	}

	@Nonnull
	public Row putAll(@Nonnull Map<String, ?> putMap) {
		checkNotNull(putMap);
		map.putAll(putMap);
		return this;
	}

	@Nonnull
	public List<String> columns() {
		return Lists.newArrayList(map.keySet());
	}

	@Nonnull
	public List<Object> values() {
		return columns().stream().map(map::get).collect(toList());
	}

	@Override
	@Nonnull
	public Iterator<Object> iterator() {
		return values().iterator();
	}

	public int size() {
		return map.size();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
