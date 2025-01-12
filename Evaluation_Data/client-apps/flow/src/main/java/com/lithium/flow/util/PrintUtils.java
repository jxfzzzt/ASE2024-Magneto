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

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class PrintUtils {
	private static final ThreadLocal<NumberFormat> COMMA_FORMAT_TL =
			ThreadLocal.withInitial(() -> NumberFormat.getNumberInstance(Locale.US));

	@Nonnull
	public static String bytes(@Nonnull Number bytes) {
		checkNotNull(bytes);
		return format(bytes, 1024, "KMGTPE", "B");
	}

	@Nonnull
	public static String count(@Nonnull Number count) {
		checkNotNull(count);
		return format(count, 1000, "KMBTQ", "");
	}

	@Nonnull
	public static String commas(@Nonnull Number count) {
		checkNotNull(count);
		return COMMA_FORMAT_TL.get().format(count);
	}

	@Nonnull
	private static String format(@Nonnull Number value, double round, @Nonnull String chars, @Nonnull String append) {
		if (value.longValue() > 0 && value.doubleValue() < 1) {
			return String.format("%.2f", value.doubleValue()) + append;
		} else if (value.longValue() < round) {
			return value.longValue() + append;
		} else {
			// show 3 significant digits
			int exponent = (int) (Math.log(value.doubleValue()) / Math.log(round));
			String decimal = String.format("%.3f", value.doubleValue() / Math.pow(round, exponent));
			return decimal.substring(0, 4).replaceAll("\\.$", "") + chars.charAt(exponent - 1) + append;
		}
	}

	@Nonnull
	public static String time(@Nonnull Number time) {
		checkNotNull(time);

		long value = time.longValue();
		long seconds = (value / 1000L) % 60;
		long minutes = (value / 60000L) % 60;
		long hours = (value / 3600000L) % 24;
		long days = value / 86400000L;
		if (days > 0) {
			return String.format("%dd%02dh%02dm%02ds", days, hours, minutes, seconds);
		} else if (hours > 0) {
			return String.format("%dh%02dm%02ds", hours, minutes, seconds);
		} else if (minutes > 0) {
			return String.format("%dm%02ds", minutes, seconds);
		} else {
			return String.format("%ds", seconds);
		}
	}
}
