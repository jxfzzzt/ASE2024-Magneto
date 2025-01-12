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

package com.lithium.flow.config.parsers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;

import com.lithium.flow.config.ConfigBuilder;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class SubtractConfigParserTest {
	@Test
	public void testSubtract() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.getString("key")).andReturn("value");
		expect(builder.setString("key", "")).andReturn(builder);
		replay(builder);

		new SubtractConfigParser().parseLine("key -= value", builder);
		verify(builder);
	}

	@Test
	public void testSubtract2() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.getString("key")).andReturn("value1 value2 value3");
		expect(builder.setString("key", "value1 value3")).andReturn(builder);
		replay(builder);

		new SubtractConfigParser().parseLine("key -= value2", builder);
		verify(builder);
	}

	@Test
	public void testSubtractMissingKey() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.getString("key")).andReturn(null);
		replay(builder);

		new SubtractConfigParser().parseLine("key -= value", builder);
		verify(builder);
	}

	@Test
	public void testNoSubtract() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);

		new SubtractConfigParser().parseLine("key += value", builder);
		verify(builder);
	}

	@Test
	public void testSecondEquals() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);
		assertFalse(new AppendConfigParser().parseLine("key = value1 -= value2", builder));
		verify(builder);
	}
}
