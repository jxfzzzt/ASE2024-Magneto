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

import java.nio.IntBuffer;
import java.util.Arrays;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class IntArray {
	private int[] array;
	private int pos;
	private final float grow;

	public IntArray() {
		this(1024, 1.5f);
	}

	public IntArray(int initialSize, float growthFactor) {
		array = new int[initialSize];
		grow = growthFactor;
	}

	public void add(int value) {
		if (pos == array.length) {
			array = Arrays.copyOf(array, (int) (array.length * grow));
		}
		array[pos++] = value;
	}

	@Nonnull
	public IntBuffer toBuffer() {
		return IntBuffer.wrap(array, 0, pos);
	}

	public int[] toArray() {
		return Arrays.copyOf(array, pos);
	}
}
