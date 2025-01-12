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

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Sleep {
	public static void forever() {
		softly(Long.MAX_VALUE);
	}

	public static boolean until(long time) {
		while (time > System.currentTimeMillis()) {
			if (!softly(time - System.currentTimeMillis())) {
				return false;
			}
		}
		return true;
	}

	public static boolean softly(long time) {
		return softly(() -> Thread.sleep(Math.max(0, time)));
	}

	public static boolean softly(@Nonnull CheckedRunnable<InterruptedException> runnable) {
		checkNotNull(runnable);
		try {
			runnable.run();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
		return true;
	}

	public static boolean randomly(long minTime, long maxTime) {
		int diff = (int) (maxTime - minTime);
		return softly(minTime + (diff > 0 ? ThreadLocalRandom.current().nextInt(diff) : 0));
	}

	public static boolean until(@Nonnull Checker checker) {
		return until(10, checker);
	}

	public static boolean until(long interval, @Nonnull Checker checker) {
		do {
			if (checker.check()) {
				return true;
			}
			if (!softly(interval)) {
				return false;
			}
		} while (!Thread.interrupted());
		return false;
	}

	public static boolean whilst(@Nonnull Checker checker) {
		return !until(checker);
	}

	public static boolean whilst(long interval, @Nonnull Checker checker) {
		return !until(interval, checker);
	}
}
