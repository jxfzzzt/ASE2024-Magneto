/*
 * Copyright 2016 Lithium Technologies, Inc.
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Execute {
	private static final ThreadFactory FACTORY = r -> {
		Thread thread = Executors.defaultThreadFactory().newThread(r);
		thread.setDaemon(true);
		return thread;
	};

	private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(FACTORY);
	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(FACTORY);

	@Nonnull
	public static ScheduledFuture<?> at(long time, @Nonnull Executable executable) {
		return in(time - System.currentTimeMillis(), executable);
	}

	@Nonnull
	public static ScheduledFuture<?> in(long time, @Nonnull Executable executable) {
		return in(time, TimeUnit.MILLISECONDS, executable);
	}

	@Nonnull
	public static ScheduledFuture<?> in(long time, @Nonnull TimeUnit unit, @Nonnull Executable executable) {
		checkNotNull(unit);
		checkNotNull(executable);
		return SCHEDULER.schedule(() -> EXECUTOR.submit(executable), time, unit);
	}
}
