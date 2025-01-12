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

package com.lithium.flow.filer.chain;

import static java.util.stream.Collectors.toList;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.ReadOnlyFiler;
import com.lithium.flow.ioc.Chain;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class ReadOnlyFilerChain implements Chain<Filer> {
	private final List<Pattern> patterns;

	public ReadOnlyFilerChain(@Nonnull Config config) {
		patterns = config.getList("readonly.hosts").stream().map(Pattern::compile).collect(toList());
	}

	@Override
	@Nonnull
	public Filer chain(@Nonnull Filer input) {
		return new ReadOnlyFiler(input, filer -> {
			try {
				String host = filer.getUri().getHost();
				return patterns.stream().anyMatch(pattern -> pattern.matcher(host).matches());
			} catch (IOException e) {
				return true;
			}
		});
	}
}
