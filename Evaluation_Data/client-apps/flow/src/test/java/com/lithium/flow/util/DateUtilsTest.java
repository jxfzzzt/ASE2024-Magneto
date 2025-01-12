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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class DateUtilsTest {
	private static final long DAY_MILLIS = 86400000L;

	@Test
	public void testNow() {
		long time = System.currentTimeMillis();
		long now = DateUtils.toMillis("NOW", () -> time);
		assertEquals(now, time);
	}

	@Test
	public void testNowRounded() {
		long time = System.currentTimeMillis();
		long now = DateUtils.toMillis("NOW~1d", () -> time);
		assertEquals(now, time / DAY_MILLIS * DAY_MILLIS);
	}

	@Test
	public void testNowMinus() {
		long time = System.currentTimeMillis();
		long now = DateUtils.toMillis("NOW - 1d", () -> time);
		assertEquals(now, time - DAY_MILLIS);
	}

	@Test
	public void testNowPlus() {
		long time = System.currentTimeMillis();
		long now = DateUtils.toMillis("NOW + 1d", () -> time);
		assertEquals(now, time + DAY_MILLIS);
	}

	@Test
	public void testNowRoundedOps() {
		long time = System.currentTimeMillis();
		long now = DateUtils.toMillis("NOW~1d + 3d - 2d", () -> time);
		assertEquals(now, time / DAY_MILLIS * DAY_MILLIS + DAY_MILLIS);
	}

	@Test
	public void testIso() {
		assertEquals(DateUtils.toMillis("2015-08-15T00:00:00+00:00"), 1439596800000L);
	}

	@Test
	public void testIsoMinus() {
		assertEquals(DateUtils.toMillis("2015-08-15T00:00:00+00:00 - 1d"), 1439596800000L - DAY_MILLIS);
	}
}
