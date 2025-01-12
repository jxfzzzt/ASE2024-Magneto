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

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class EqualsConfigParser implements ConfigParser {
	@Override
	public boolean parseLine(@Nonnull String line, @Nonnull ConfigBuilder builder) {
		checkNotNull(line);
		checkNotNull(builder);

		int index = line.indexOf("=");
		if (index > -1) {
			String key = line.substring(0, index).trim();
			String value = line.substring(index + 1).trim();
			builder.setString(key, value);
			return true;
		} else {
			return false;
		}
	}
}
