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

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.FilteredFiler;
import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.RegexSubpathPredicate;
import com.lithium.flow.ioc.Chain;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class SubpathsFilerChain implements Chain<Filer> {
	private final Predicate<Record> predicate;

	public SubpathsFilerChain(@Nonnull Config config) {
		checkNotNull(config);

		Predicate<Record> composite = r -> false;
		for (String subpath : config.getList("subpaths")) {
			composite = composite.or(new RegexSubpathPredicate(subpath));
		}
		predicate = composite;
	}

	@Override
	@Nonnull
	public Filer chain(@Nonnull Filer input) {
		return new FilteredFiler(input, predicate);
	}
}
