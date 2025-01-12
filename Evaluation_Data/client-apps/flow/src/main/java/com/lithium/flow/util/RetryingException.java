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

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;

/**
 * @author Matt Ayres
 */
class RetryingException extends RuntimeException {
	private final List<Exception> exceptions;

	public RetryingException(@Nonnull List<Exception> exceptions) {
		this.exceptions = checkNotNull(exceptions);
	}

	@Nonnull
	public List<Exception> getExceptions() {
		return exceptions;
	}

	@Override
	public String getMessage() {
		Multiset<String> multiset = LinkedHashMultiset.create();
		exceptions.forEach(e -> multiset.add(e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
		return "Failed after " + exceptions.size() + " tries: " + multiset;
	}

	@Override
	public Throwable getCause() {
		return exceptions.get(0);
	}
}
