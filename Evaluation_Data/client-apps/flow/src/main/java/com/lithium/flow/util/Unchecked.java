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

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Unchecked {
	public static <T> T get(@Nonnull CheckedSupplier<T, ? extends Exception> supplier) {
		checkNotNull(supplier);
		try {
			return supplier.get();
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	public static void run(@Nonnull CheckedRunnable<? extends Exception> runnable) {
		checkNotNull(runnable);
		try {
			runnable.run();
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	public static void runAsync(@Nonnull CheckedRunnable<? extends Exception> runnable) {
		checkNotNull(runnable);
		CompletableFuture.runAsync(() -> run(runnable));
	}
}
