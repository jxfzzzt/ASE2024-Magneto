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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * @author Matt Ayres
 */
public class Key {
	private static final Joiner ID_JOINER = Joiner.on(':');
	private static final Splitter ID_SPLITTER = Splitter.on(':');

	private final List<Object> values;
	private final String id;
	private final boolean auto;

	private Key(@Nonnull List<Object> values, boolean auto) {
		this.values = checkNotNull(values);
		this.id = ID_JOINER.join(values);
		this.auto = auto;
	}

	@Nonnull
	public List<Object> list() {
		return values;
	}

	@Nonnull
	public Object[] array() {
		return values.toArray();
	}

	@Nonnull
	public String id() {
		return id;
	}

	public boolean isAuto() {
		return auto;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Nonnull
	public static Key of(@Nonnull Object value) {
		checkNotNull(value);
		return new Key(Collections.singletonList(value), false);
	}

	@Nonnull
	public static Key of(@Nonnull Object firstValue, Object... moreValues) {
		checkNotNull(firstValue);
		for (Object value : moreValues) {
			checkNotNull(value);
		}

		List<Object> values = new ArrayList<>();
		values.add(firstValue);
		Collections.addAll(values, moreValues);
		return new Key(values, false);
	}

	@Nonnull
	public static Key from(@Nonnull String id) {
		checkNotNull(id);
		return new Key(Lists.newArrayList(ID_SPLITTER.split(id).iterator()), false);
	}

	@Nonnull
	public static Key auto() {
		return new Key(Collections.singletonList(UUID.randomUUID().toString()), true);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Key that = (Key) obj;
		return id.equals(that.id);

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
