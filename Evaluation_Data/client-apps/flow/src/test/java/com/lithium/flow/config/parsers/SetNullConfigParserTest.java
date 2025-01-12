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
public class SetNullConfigParserTest {
	@Test
	public void testSetNull() {
		testSetNull("foo 0=", "foo");
		testSetNull("bar 0= bar", "bar");
	}

	private void testSetNull(String line, String key) {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.removeKey(key)).andReturn(builder);
		replay(builder);
		assertTrue(new SetNullConfigParser().parseLine(line, builder));
		verify(builder);
	}

	@Test
	public void testNoSetNull() {
		testNoSetNull("foo = bar");
		testNoSetNull("foo !=");
		testNoSetNull("foo =0");
	}

	private void testNoSetNull(String line) {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);
		assertFalse(new SetNullConfigParser().parseLine(line, builder));
		verify(builder);
	}

	@Test
	public void testSecondEquals() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);
		assertFalse(new AppendConfigParser().parseLine("key = value1 0= value2", builder));
		verify(builder);
	}
}
