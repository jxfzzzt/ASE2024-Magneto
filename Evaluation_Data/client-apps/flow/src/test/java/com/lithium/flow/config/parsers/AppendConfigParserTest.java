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
import static org.junit.Assert.assertTrue;

import com.lithium.flow.config.ConfigBuilder;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class AppendConfigParserTest {
	@Test
	public void testAppendNull() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.containsKey("key")).andReturn(false);
		expect(builder.setString("key", "value1")).andReturn(builder);
		replay(builder);

		AppendConfigParser parser = new AppendConfigParser();
		assertTrue(parser.parseLine("key += value1", builder));
		verify(builder);
	}

	@Test
	public void testAppendString() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.containsKey("key")).andReturn(true);
		expect(builder.getString("key")).andReturn("value1");
		expect(builder.setString("key", "value1 value2")).andReturn(builder);
		replay(builder);

		AppendConfigParser parser = new AppendConfigParser();
		assertTrue(parser.parseLine("key += value2", builder));
		verify(builder);
	}

	@Test
	public void testEmptyAppend() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.containsKey("key")).andReturn(false);
		expect(builder.setString("key", "")).andReturn(builder);
		expect(builder.containsKey("key")).andReturn(true);
		expect(builder.getString("key")).andReturn("");
		expect(builder.setString("key", "value1")).andReturn(builder);
		replay(builder);

		AppendConfigParser parser = new AppendConfigParser();
		assertTrue(parser.parseLine("key += ", builder));
		assertTrue(parser.parseLine("key += value1", builder));
		verify(builder);
	}

	@Test
	public void testNoAppend() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);
		assertFalse(new AppendConfigParser().parseLine("foo = bar", builder));
		verify(builder);
	}

	@Test
	public void testSecondEquals() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);
		assertFalse(new AppendConfigParser().parseLine("key = value1 += value2", builder));
		verify(builder);
	}
}
