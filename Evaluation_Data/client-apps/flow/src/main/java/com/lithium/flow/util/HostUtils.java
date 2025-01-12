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
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
public class HostUtils {
	@Nonnull
	public static List<String> expand(@Nonnull String expression) {
		checkNotNull(expression);

		int index1 = expression.indexOf("[");
		int index2 = expression.indexOf("]", index1);
		if (index1 > -1 && index2 > -1 && index2 < expression.length()) {
			String prefix = expression.substring(0, index1);
			String ranges = expression.substring(index1 + 1, index2);
			String postfix = expression.substring(index2 + 1);

			List<String> includes = new ArrayList<>();
			List<String> excludes = new ArrayList<>();

			for (String range : Splitter.on(CharMatcher.anyOf(",;")).split(ranges)) {
				List<String> list = range.startsWith("!") ? excludes : includes;
				range = range.replace("!", "");

				Iterator<String> it = Splitter.on('-').split(range).iterator();
				String first = it.next();
				String last = it.hasNext() ? it.next() : first;
				String format = "%0" + first.length() + "d";
				IntStream.rangeClosed(parseInt(first), parseInt(last)).forEach(next ->
						list.add(prefix + String.format(format, next) + postfix));
			}

			includes.removeAll(excludes);
			return expand(includes);
		} else {
			return Collections.singletonList(expression);
		}
	}

	@Nonnull
	public static List<String> expand(@Nonnull List<String> expressions) {
		return expressions.stream().flatMap(expression -> expand(expression).stream()).collect(toList());
	}
}
