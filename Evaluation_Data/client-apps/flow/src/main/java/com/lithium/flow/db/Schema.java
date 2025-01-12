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

import com.lithium.flow.util.CheckedBiConsumer;
import com.lithium.flow.util.CheckedConsumer;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public interface Schema extends Closeable {
	@Nonnull
	Connection getConnection() throws SQLException;

	@Nullable
	<T> T select(@Nonnull String query, Object... parameters) throws SQLException;

	@Nullable
	String selectString(@Nonnull String query, Object... parameters) throws SQLException;

	int selectInt(@Nonnull String query, Object... parameters) throws SQLException;

	long selectLong(@Nonnull String query, Object... parameters) throws SQLException;

	@Nonnull
	<T> List<T> selectList(@Nonnull String query, Object... parameters) throws SQLException;

	@Nonnull
	List<String> selectStringList(@Nonnull String query, Object... parameters) throws SQLException;

	@Nonnull
	List<Integer> selectIntList(@Nonnull String query, Object... parameters) throws SQLException;

	@Nonnull
	List<Long> selectLongList(@Nonnull String query, Object... parameters) throws SQLException;

	@Nonnull
	<T> List<T> queryList(@Nonnull String query,
			@Nonnull CheckedBiConsumer<ResultSet, List<T>, SQLException> consumer,
			Object... parameters) throws SQLException;

	@Nonnull
	<T> Set<T> querySet(@Nonnull String query,
			@Nonnull CheckedBiConsumer<ResultSet, Set<T>, SQLException> consumer,
			Object... parameters) throws SQLException;

	@Nonnull
	<K, V> Map<K, V> queryMap(@Nonnull String query,
			@Nonnull CheckedBiConsumer<ResultSet, Map<K, V>, SQLException> consumer,
			Object... parameters) throws SQLException;

	<E extends Exception> void queryRows(@Nonnull String query,
			@Nonnull CheckedConsumer<ResultSet, E> consumer,
			Object... parameters) throws SQLException, E;

	int update(@Nonnull String query, Object... parameters) throws SQLException;
}
