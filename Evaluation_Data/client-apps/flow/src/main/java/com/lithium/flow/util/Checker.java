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

import static com.google.common.base.Preconditions.*;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public interface Checker {
	boolean check();

	@Nonnull
	static Checker compositeOr(@Nonnull List<Checker> checkers) {
		checkNotNull(checkers);
		return () -> checkers.stream().anyMatch(Checker::check);
	}

	@Nonnull
	static Checker compositeAnd(@Nonnull List<Checker> checkers) {
		checkNotNull(checkers);
		return () -> checkers.stream().allMatch(Checker::check);
	}
}
