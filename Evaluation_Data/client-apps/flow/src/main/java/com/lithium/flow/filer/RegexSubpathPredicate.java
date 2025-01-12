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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
public class RegexSubpathPredicate implements Predicate<Record> {
	private final Splitter splitter = Splitter.on('/');
	private final List<Pattern> patterns = new ArrayList<>();

	public RegexSubpathPredicate(@Nonnull String pathRegex) {
		checkNotNull(pathRegex);
		Splitter.on('/').split(pathRegex).forEach(part -> patterns.add(Pattern.compile(part)));
	}

	@Override
	public boolean test(@Nullable Record record) {
		if (record == null) {
			return false;
		}

		int index = 0;
		for (String part : splitter.split(record.getPath())) {
			if (index < patterns.size() && !patterns.get(index++).matcher(part).matches()) {
				return false;
			}
		}
		return true;
	}
}
