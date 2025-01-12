/*
 * Copyright 2016 Lithium Technologies, Inc.
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

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.util.CsvFormats;
import com.lithium.flow.util.Unchecked;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.csv.CSVPrinter;

/**
 * @author Matt Ayres
 */
public class CsvOutputTable implements Table {
	private final CSVPrinter printer;
	private boolean needsHeader;

	public CsvOutputTable(@Nonnull OutputStream out) throws IOException {
		this(out, Configs.empty());
	}

	public CsvOutputTable(@Nonnull OutputStream out, @Nonnull Config config) throws IOException {
		Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
		printer = new CSVPrinter(writer, CsvFormats.fromConfig(config));
		needsHeader = config.getBoolean("csv.autoHeader", false);
	}

	@Override
	public void putRow(@Nonnull Row row) {
		Unchecked.run(() -> {
			synchronized (printer) {
				addHeader(row);
				printer.printRecord(row);
			}
		});
	}

	@Override
	public void putRows(@Nonnull List<Row> rows) {
		Unchecked.run(() -> {
			synchronized (printer) {
				addHeader(rows.get(0));
				for (Row row : rows) {
					printer.printRecord(row);
				}
			}
		});
	}

	private void addHeader(@Nonnull Row row) throws IOException {
		if (needsHeader) {
			printer.printRecord(row.columns());
			needsHeader = false;
		}
	}

	@Override
	@Nonnull
	public Row getRow(@Nonnull Key key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteRow(@Nonnull Key key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		printer.close();
	}
}
