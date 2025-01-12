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

package com.lithium.flow.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class MemoryTableTest {
	@Test
	public void test() {
		Table table = new MemoryTable();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				table.putCell(Key.of(i), String.valueOf(j), i * j);
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Integer value = table.getCell(Key.of(i), String.valueOf(j), Integer.class);
				assertNotNull(value);
				assertEquals(i * j, value.intValue());
			}
		}
	}
}
