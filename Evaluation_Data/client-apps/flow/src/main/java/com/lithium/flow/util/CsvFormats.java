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

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

/**
 * @author Matt Ayres
 */
public class CsvFormats {
	@Nonnull
	public static CSVFormat fromConfig(@Nonnull Config config) {
		checkNotNull(config);
		switch (config.getString("csv.format", "default")) {
			case "default":
				return CSVFormat.DEFAULT.withHeader(getHeader(config, "csv.header"));
			case "excel":
				return CSVFormat.EXCEL;
			case "mysql":
				return CSVFormat.MYSQL;
			case "rfc4180":
				return CSVFormat.RFC4180;
			case "tdf":
				return CSVFormat.TDF;
			case "custom":
				return CSVFormat.newFormat(getChar(config))
						.withAllowMissingColumnNames(getBoolean(config, "csv.allowMissingColumnNames"))
						.withCommentMarker(getChar(config, "csv.commentMarker"))
						.withEscape(getChar(config, "csv.escape"))
						.withHeader(getHeader(config, "csv.header"))
						.withIgnoreEmptyLines(getBoolean(config, "csv.ignoreEmptyLines"))
						.withIgnoreSurroundingSpaces(getBoolean(config, "csv.ignoreSurroundingSpaces"))
						.withNullString(getString(config, "csv.nullString"))
						.withQuote(getChar(config, "csv.quote"))
						.withQuoteMode(getQuoteMode(config))
						.withRecordSeparator(getString(config, "csv.recordSeparator"))
						.withSkipHeaderRecord(getBoolean(config, "csv.skipHeaderRecord"));
			default:
				return CSVFormat.DEFAULT;
		}
	}

	private static char getChar(@Nonnull Config config) {
		String value = config.getString("csv.delimiter");
		return value.isEmpty() ? ',' : value.charAt(0);
	}

	private static Character getChar(@Nonnull Config config, @Nonnull String key) {
		String value = config.getString(key);
		return value.isEmpty() ? null : value.charAt(0);
	}

	private static boolean getBoolean(@Nonnull Config config, @Nonnull String key) {
		return config.containsKey(key) && config.getBoolean(key);
	}

	@Nullable
	public static String[] getHeader(@Nonnull Config config, @Nonnull String key) {
		if (!config.containsKey(key)) {
			return null;
		} else {
			List<String> headers = config.getList(key);
			return headers.toArray(new String[0]);
		}
	}

	@Nullable
	private static String getString(@Nonnull Config config, @Nonnull String key) {
		return config.containsKey(key) ? config.getString(key) : null;
	}

	@Nullable
	private static QuoteMode getQuoteMode(@Nonnull Config config) {
		switch (config.getString("csv.quoteMode", "default")) {
			case "all":
				return QuoteMode.ALL;
			case "minimal":
				return QuoteMode.MINIMAL;
			case "non_numeric":
				return QuoteMode.NON_NUMERIC;
			case "none":
				return QuoteMode.NONE;
			default:
				return null;
		}
	}
}
