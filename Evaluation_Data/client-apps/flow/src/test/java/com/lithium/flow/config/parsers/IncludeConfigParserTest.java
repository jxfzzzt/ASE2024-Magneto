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

import java.io.IOException;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class IncludeConfigParserTest {
	@Test
	public void testInclude() throws IOException {
		testInclude("!include foo/bar.config", "foo/bar.config");
		testInclude("!include\tfoo/baz.config", "foo/baz.config");
		testInclude("!include  foo/baz.config", "foo/baz.config");
		testInclude("!include foo/bar.config ", "foo/bar.config");
	}

	private void testInclude(String line, String include) throws IOException {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.include(include)).andReturn(builder);
		replay(builder);
		assertTrue(new IncludeConfigParser().parseLine(line, builder));
		verify(builder);
	}

	@Test
	public void testNoInclude() throws IOException {
		testNoInclude("#include foo/bar.config");
		testNoInclude(" !include foo/bar.config");
	}

	private void testNoInclude(String line) throws IOException {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);
		assertFalse(new IncludeConfigParser().parseLine(line, builder));
		verify(builder);
	}

	@Test
	public void testPrefixInclude() throws IOException {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		expect(builder.pushPrefix("arf")).andReturn(builder);
		expect(builder.include("foo/bar.config")).andReturn(builder);
		expect(builder.popPrefix()).andReturn(builder);
		replay(builder);
		assertTrue(new IncludeConfigParser().parseLine("!include(arf) foo/bar.config", builder));
		verify(builder);
	}
}
