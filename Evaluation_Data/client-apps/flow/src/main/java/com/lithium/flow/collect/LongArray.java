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

package com.lithium.flow.collect;

import java.nio.LongBuffer;
import java.util.Arrays;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class LongArray {
	private long[] array;
	private int pos;
	private final float grow;

	public LongArray() {
		this(1024, 1.5f);
	}

	public LongArray(int initialSize, float growthFactor) {
		array = new long[initialSize];
		grow = growthFactor;
	}

	public void add(long value) {
		if (pos == array.length) {
			array = Arrays.copyOf(array, (int) (array.length * grow));
		}
		array[pos++] = value;
	}

	@Nonnull
	public LongBuffer toBuffer() {
		return LongBuffer.wrap(array, 0, pos);
	}

	public long[] toArray() {
		return Arrays.copyOf(array, pos);
	}
}
