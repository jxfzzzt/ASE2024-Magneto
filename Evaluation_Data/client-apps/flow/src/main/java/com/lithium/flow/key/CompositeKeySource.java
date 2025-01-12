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

package com.lithium.flow.key;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

import java.security.Key;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class CompositeKeySource implements KeySource {
	private final List<KeySource> sources;

	public CompositeKeySource(@Nonnull List<KeySource> sources) {
		this.sources = checkNotNull(sources);
	}

	@Override
	@Nonnull
	public List<Key> getKeys(@Nonnull String name) {
		checkNotNull(name);
		return sources.stream().flatMap(source -> source.getKeys(name).stream()).collect(toList());
	}
}
