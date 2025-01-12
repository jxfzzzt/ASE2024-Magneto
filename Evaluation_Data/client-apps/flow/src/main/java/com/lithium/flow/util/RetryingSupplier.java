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

import java.util.function.IntFunction;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class RetryingSupplier<T, E extends Exception> implements CheckedSupplier<T, E> {
	private final RetryingFunction<String, T, E> function;

	public RetryingSupplier(int tries, @Nonnull CheckedSupplier<T, E> supplier) {
		this(tries, 0, supplier);
	}

	public RetryingSupplier(int tries, long delay, @Nonnull CheckedSupplier<T, E> supplier) {
		this(tries, i -> delay, supplier);
	}

	public RetryingSupplier(int tries, @Nonnull IntFunction<Number> delay, @Nonnull CheckedSupplier<T, E> supplier) {
		function = new RetryingFunction<>(tries, delay, x -> supplier.get());
	}

	public T get() throws E {
		return function.get("");
	}
}
