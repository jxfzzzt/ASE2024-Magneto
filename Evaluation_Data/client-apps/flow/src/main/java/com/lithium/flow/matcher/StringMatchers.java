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

package com.lithium.flow.matcher;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

import com.lithium.flow.config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @author Matt Ayres
 */
public class StringMatchers {
	@Nonnull
	public static StringMatcher fromConfig(@Nonnull Config config, @Nonnull String key) {
		checkNotNull(config);
		checkNotNull(key);
		return fromList(config.getList(key, Splitter.on(' ')));
	}

	@Nonnull
	public static StringMatcher fromInputStream(@Nonnull InputStream in) {
		try (InputStream tryIn = checkNotNull(in)) {
			return fromList(IOUtils.readLines(tryIn, StandardCharsets.UTF_8).stream()
					.filter(line -> !line.startsWith("#")).collect(toList()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	public static StringMatcher fromList(@Nonnull List<String> list) {
		Multimap<String, String> multimap = HashMultimap.create();
		for (String value : list) {
			if (value.equals("*")) {
				return new AllStringMatcher();
			}

			int index = value.indexOf(':');
			if (index == -1 || index >= value.length() - 1) {
				multimap.put("exact", value);
			} else {
				int index2 = value.indexOf("?[");
				int index3 = value.indexOf("]:", index2);
				if (index2 > -1 && index3 > -1 && index2 < index) {
					index = index2 + 1;
				}

				String type = value.substring(0, index);
				String param = value.substring(index + 1);
				multimap.put(type, param);
			}
		}

		List<StringMatcher> quickMatchers = new ArrayList<>(buildList(multimap, "len", LenStringMatcher::new));
		Collection<String> exacts = multimap.get("exact");
		if (exacts.size() == 1) {
			quickMatchers.add(new ExactStringMatcher(exacts.iterator().next()));
		} else if (exacts.size() > 1) {
			quickMatchers.add(new ExactSetStringMatcher(new HashSet<>(exacts)));
		}
		quickMatchers.addAll(buildList(multimap, "prefix", PrefixStringMatcher::new));
		quickMatchers.addAll(buildList(multimap, "suffix", SuffixStringMatcher::new));
		quickMatchers.addAll(buildList(multimap, "contains", ContainsStringMatcher::new));

		List<StringMatcher> lowerMatchers = new ArrayList<>();
		lowerMatchers.addAll(buildList(multimap, "lower.prefix", PrefixStringMatcher::new));
		lowerMatchers.addAll(buildList(multimap, "lower.suffix", SuffixStringMatcher::new));
		lowerMatchers.addAll(buildList(multimap, "lower.contains", ContainsStringMatcher::new));

		List<StringMatcher> regexMatchers = new ArrayList<>();
		regexMatchers.addAll(buildList(multimap, "regex", RegexStringMatcher::new));
		regexMatchers.addAll(buildList(multimap, "lower.regex", LowerRegexStringMatcher::new));

		List<StringMatcher> allMatchers = new ArrayList<>();
		allMatchers.add(buildComposite(quickMatchers, false));
		allMatchers.add(buildComposite(lowerMatchers, true));
		allMatchers.add(buildComposite(regexMatchers, false));
		return buildComposite(allMatchers, false);
	}

	@Nonnull
	private static StringMatcher buildComposite(@Nonnull List<StringMatcher> matchers, boolean lower) {
		matchers = matchers.stream().filter(matcher -> !(matcher instanceof NoStringMatcher)).collect(toList());
		if (matchers.size() == 0) {
			return new NoStringMatcher();
		} else if (matchers.size() == 1) {
			StringMatcher matcher = matchers.iterator().next();
			return lower ? new LowerStringMatcher(matcher) : matcher;
		} else {
			return new CompositeOrStringMatcher(matchers, lower);
		}
	}

	@Nonnull
	private static List<StringMatcher> buildList(@Nonnull Multimap<String, String> multimap, @Nonnull String group,
			@Nonnull Function<String, StringMatcher> function) {
		List<StringMatcher> list = new ArrayList<>();
		multimap.get(group).stream().map(function).forEach(list::add);
		multimap.get(group + "?").stream().map(input -> buildConditional(input, function)).forEach(list::add);
		return list;
	}

	@Nonnull
	private static StringMatcher buildConditional(@Nonnull String input,
			@Nonnull Function<String, StringMatcher> function) {
		int index1 = input.indexOf("?[");
		int index2 = input.indexOf("]:");
		String cond = input.substring(index1 + 1, index2);
		String regex = input.substring(index2 + 2);
		StringMatcher condMatcher = fromList(Collections.singletonList(cond));
		StringMatcher funcMatcher = function.apply(regex);
		return new CompositeAndStringMatcher(Arrays.asList(condMatcher, funcMatcher), false);
	}
}
