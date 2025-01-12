/*
 * TableUtils.java
 * Created on Jul 28, 2016
 *
 * Copyright 2016 Lithium Technologies, Inc.
 * San Francisco, California, U.S.A.  All Rights Reserved.
 *
 * This software is the  confidential and proprietary information
 * of  Lithium  Technologies,  Inc.  ("Confidential Information")
 * You shall not disclose such Confidential Information and shall
 * use  it  only in  accordance  with  the terms of  the  license
 * agreement you entered into with Lithium.
 */

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.compress.Coders;
import com.lithium.flow.config.Config;
import com.lithium.flow.config.exception.IllegalConfigException;
import com.lithium.flow.db.Databases;
import com.lithium.flow.db.Schema;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Filers;
import com.lithium.flow.table.CsvOutputTable;
import com.lithium.flow.table.NoTable;
import com.lithium.flow.table.SqlTable;
import com.lithium.flow.table.Table;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class TableUtils {
	@Nonnull
	public static Table buildTable(@Nonnull Config config) throws IOException, SQLException {
		checkNotNull(config);

		String table = config.getString("table");
		switch (table) {
			case "csv":
				String path = config.getString("csv.path");
				Filer filer = Filers.buildFiler(config);
				OutputStream out = filer.writeFile(path);
				out = Coders.getCoder(path).wrapOut(out);
				out = new BufferedOutputStream(out, 65536);
				return new CsvOutputTable(out, config);

			case "elastic":
				return ElasticUtils.buildTable(config);

			case "elastic-rest":
				return ElasticRestUtils.buildTable(config);

			case "sql":
				Schema schema = Databases.buildSchema(config);
				return new SqlTable(schema, config.getString("sql.table"), config.getList("sql.columns"));

			case "none":
				return new NoTable();

			default:
				throw new IllegalConfigException("table", table, "string", null);
		}
	}
}
