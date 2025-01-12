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

import com.lithium.flow.db.Schema;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Unchecked;
import com.lithium.flow.util.UncheckedException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class SqlTable implements Table {
	private static final Logger log = Logs.getLogger();

	private final Schema schema;
	private final String table;
	private final List<String> keyColumns;
	private final List<String> validColumns;

	public SqlTable(@Nonnull Schema schema, @Nonnull String table, List<String> keyColumns) throws SQLException {
		this.schema = checkNotNull(schema);
		this.table = checkNotNull(table);
		this.keyColumns = checkNotNull(keyColumns);
		this.validColumns = buildColumns(schema, table);
	}

	@Override
	@Nullable
	public <T> T getCell(@Nonnull Key key, @Nonnull String column, @Nonnull Class<T> clazz) {
		checkNotNull(key);
		checkValidColumn(column);
		checkNotNull(clazz);

		String query = buildSelectQuery(column);
		log.debug("select: {} {}", query, key.list());

		try {
			return schema.select(query, key.array());
		} catch (SQLException e) {
			throw new UncheckedException(e);
		}
	}

	@Override
	@Nonnull
	public Row getRow(@Nonnull Key key) {
		String query = buildSelectQuery("*");
		log.debug("select: {} {}", query, key.list());

		return Unchecked.get(() -> {
			Row row = new Row(key);

			schema.queryRows(query, rs -> {
				ResultSetMetaData meta = rs.getMetaData();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String column = meta.getColumnName(i);
					row.putCell(column, rs.getObject(i));
				}
			}, key.array());

			return row;
		});
	}

	@Override
	public void putRow(@Nonnull Row row) {
		writeRow(row, this::buildInsertQuery);
	}

	@Override
	public void putRows(@Nonnull List<Row> rows) {
		checkNotNull(rows);

		List<String> columns = rows.get(0).columns();
		columns.forEach(this::checkValidColumn);
		String query = buildInsertQuery(columns);

		Unchecked.run(() -> {
			try (Connection con = schema.getConnection()) {
				con.setAutoCommit(false);

				try (PreparedStatement ps = con.prepareStatement(query)) {
					for (Row row : rows) {
						List<Object> params = new ArrayList<>();
						params.addAll(row.values());
						params.addAll(row.getKey().list());

						log.debug("insert: {} {}", query, params);

						int i = 1;
						for (Object param : params) {
							ps.setObject(i++, param);
						}
						ps.addBatch();
					}

					ps.executeBatch();
				}

				con.commit();
			}
		});
	}

	@Override
	public void updateRow(@Nonnull Row row) {
		writeRow(row, this::buildUpdateQuery);
	}

	private void writeRow(@Nonnull Row row, @Nonnull Function<List<String>, String> function) {
		checkNotNull(row);
		checkNotNull(function);

		List<String> columns = row.columns();
		columns.forEach(this::checkValidColumn);

		List<Object> params = new ArrayList<>();
		params.addAll(row.values());
		params.addAll(row.getKey().list());

		String query = function.apply(columns);
		log.debug("write: {} {}", query, params);

		Unchecked.run(() -> schema.update(query, params.toArray()));
	}

	@Override
	public void deleteRow(@Nonnull Key key) {
		String query = buildDeleteQuery();
		log.debug("delete: {} {}", query, key.list());
		Unchecked.run(() -> schema.update(query, key.array()));
	}

	private void checkValidColumn(@Nonnull String column) {
		if (!validColumns.contains(column)) {
			throw new IllegalArgumentException("invalid column: " + column);
		}
	}

	@Nonnull
	private String buildSelectQuery(@Nonnull String column) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ").append(column).append(" FROM ").append(table);
		appendWhere(sb);
		return sb.toString();
	}

	@Nonnull
	private String buildUpdateQuery(@Nonnull List<String> columns) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(table).append(" SET ");
		for (int i = 0; i < columns.size(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(columns.get(i)).append(" = ?");
		}
		return appendWhere(sb);
	}

	@Nonnull
	private String appendWhere(@Nonnull StringBuilder sb) {
		sb.append(" WHERE ");
		for (int i = 0; i < keyColumns.size(); i++) {
			if (i > 0) {
				sb.append(" AND ");
			}
			sb.append(keyColumns.get(i)).append(" = ?");
		}
		return sb.toString();
	}

	@Nonnull
	private String buildInsertQuery(@Nonnull List<String> columns) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(table).append(" (");

		for (int i = 0; i < columns.size(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(columns.get(i));
		}
		for (String column : keyColumns) {
			sb.append(", ").append(column);
		}

		sb.append(") VALUES (");

		for (int i = 0; i < columns.size() + keyColumns.size(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("?");
		}

		sb.append(")");
		return sb.toString();
	}

	@Nonnull
	private String buildDeleteQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(table);
		return appendWhere(sb);
	}

	@Nonnull
	private static List<String> buildColumns(@Nonnull Schema schema, @Nonnull String table) throws SQLException {
		List<String> list = new ArrayList<>();
		try (Connection con = schema.getConnection()) {
			DatabaseMetaData meta = con.getMetaData();
			try (ResultSet rs = meta.getColumns(null, null, table, null)) {
				while (rs.next()) {
					String column = rs.getString("COLUMN_NAME");
					list.add(column);
				}
			}
		}
		return list;
	}

	@Override
	public void close() throws IOException {
		schema.close();
	}
}
