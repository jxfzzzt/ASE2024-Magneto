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

import static java.util.concurrent.CompletableFuture.runAsync;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Lazy<T> extends CheckedLazy<T, Exception> {
	public Lazy(@Nonnull CheckedSupplier<T, Exception> supplier) {
		super(supplier);
	}

	@Nonnull
	public Lazy<T> eager() {
		runAsync(this::get);
		return this;
	}

	@Nonnull
	public T get() {
		try {
			return super.get();
		} catch (Exception e) {
			throw new RuntimeException("lazy call failed", e);
		}
	}
}
