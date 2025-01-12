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

package com.lithium.flow.config.parsers;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.ConfigBuilder;
import com.lithium.flow.config.ConfigParser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class IncludeConfigParser implements ConfigParser {
	private static final Pattern prefixPattern = Pattern.compile("!include\\((.*)\\)[ \t]+(.+?)[ \t]*");
	private static final Pattern normalPattern = Pattern.compile("!include[ \t]+(.+?)[ \t]*");

	@Override
	public boolean parseLine(@Nonnull String line, @Nonnull ConfigBuilder builder) throws IOException {
		checkNotNull(line);
		checkNotNull(builder);

		Matcher prefixMatcher = prefixPattern.matcher(line);
		if (prefixMatcher.matches()) {
			builder.pushPrefix(prefixMatcher.group(1));
			builder.include(prefixMatcher.group(2));
			builder.popPrefix();
			return true;
		}

		Matcher normalMatcher = normalPattern.matcher(line);
		if (normalMatcher.matches()) {
			builder.include(normalMatcher.group(1));
			return true;
		}

		return false;
	}
}
