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

package com.lithium.flow.db;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.util.CheckedBiConsumer;
import com.lithium.flow.util.CheckedConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public abstract class AbstractSchema implements Schema {
	@Override
	@Nullable
	@SuppressWarnings("unchecked")
	public <T> T select(@Nonnull String query, Object... parameters) throws SQLException {
		AtomicReference<Object> value = new AtomicReference<>();
		queryRows(query, rs -> value.set(rs.getObject(1)), parameters);
		return (T) value.get();
	}

	@Override
	@Nullable
	public String selectString(@Nonnull String query, Object... parameters) throws SQLException {
		AtomicReference<String> value = new AtomicReference<>();
		queryRows(query, rs -> value.set(rs.getString(1)), parameters);
		return value.get();
	}

	@Override
	public int selectInt(@Nonnull String query, Object... parameters) throws SQLException {
		AtomicInteger value = new AtomicInteger();
		queryRows(query, rs -> value.set(rs.getInt(1)), parameters);
		return value.get();
	}

	@Override
	public long selectLong(@Nonnull String query, Object... parameters) throws SQLException {
		AtomicLong value = new AtomicLong();
		queryRows(query, rs -> value.set(rs.getLong(1)), parameters);
		return value.get();
	}

	@Override
	@Nonnull
	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(@Nonnull String query, Object... parameters) throws SQLException {
		return queryList(query, (rs, list) -> list.add((T) rs.getObject(1)), parameters);
	}

	@Override
	@Nonnull
	public List<String> selectStringList(@Nonnull String query, Object... parameters) throws SQLException {
		return queryList(query, (rs, list) -> list.add(rs.getString(1)), parameters);
	}

	@Override
	@Nonnull
	public List<Integer> selectIntList(@Nonnull String query, Object... parameters) throws SQLException {
		return queryList(query, (rs, list) -> list.add(rs.getInt(1)), parameters);
	}

	@Override
	@Nonnull
	public List<Long> selectLongList(@Nonnull String query, Object... parameters) throws SQLException {
		return queryList(query, (rs, list) -> list.add(rs.getLong(1)), parameters);
	}

	@Override
	@Nonnull
	public <T> List<T> queryList(@Nonnull String query,
			@Nonnull CheckedBiConsumer<ResultSet, List<T>, SQLException> consumer,
			Object... parameters) throws SQLException {
		checkNotNull(query);
		checkNotNull(consumer);
		List<T> list = new ArrayList<>();
		queryRows(query, rs -> consumer.accept(rs, list), parameters);
		return list;
	}

	@Override
	@Nonnull
	public <T> Set<T> querySet(@Nonnull String query,
			@Nonnull CheckedBiConsumer<ResultSet, Set<T>, SQLException> consumer,
			Object... parameters) throws SQLException {
		checkNotNull(query);
		checkNotNull(consumer);
		Set<T> set = new HashSet<>();
		queryRows(query, rs -> consumer.accept(rs, set), parameters);
		return set;
	}

	@Override
	@Nonnull
	public <K, V> Map<K, V> queryMap(@Nonnull String query,
			@Nonnull CheckedBiConsumer<ResultSet, Map<K, V>, SQLException> consumer,
			Object... parameters) throws SQLException {
		checkNotNull(query);
		checkNotNull(consumer);
		Map<K, V> map = new HashMap<>();
		queryRows(query, rs -> consumer.accept(rs, map), parameters);
		return map;
	}

	@Override
	public <E extends Exception> void queryRows(@Nonnull String query,
			@Nonnull CheckedConsumer<ResultSet, E> consumer, Object... parameters)
			throws SQLException, E {
		checkNotNull(query);
		checkNotNull(consumer);
		try (Connection con = getConnection()) {
			try (PreparedStatement ps = readQuery(con, query)) {
				int i = 1;
				for (Object parameter : parameters) {
					ps.setObject(i++, parameter);
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						consumer.accept(rs);
					}
				}
			}
		}
	}

	@Nonnull
	protected PreparedStatement readQuery(@Nonnull Connection con, @Nonnull String query) throws SQLException {
		return con.prepareStatement(query);
	}

	@Override
	public int update(@Nonnull String query, Object... parameters) throws SQLException {
		checkNotNull(query);
		try (Connection con = getConnection()) {
			try (PreparedStatement ps = con.prepareStatement(query)) {
				int i = 1;
				for (Object parameter : parameters) {
					ps.setObject(i++, parameter);
				}
				return ps.executeUpdate();
			}
		}
	}
}
