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
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.lithium.flow.config.ConfigBuilder;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class CommentConfigParserTest {
	@Test
	public void test() {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		replay(builder);

		CommentConfigParser parser = new CommentConfigParser();
		assertFalse(parser.parseLine("foo = bar", builder));
		assertFalse(parser.parseLine("", builder));
		assertFalse(parser.parseLine("/ foo = bar", builder));
		assertTrue(parser.parseLine("# foo = bar", builder));
		assertTrue(parser.parseLine("// foo = bar", builder));
		assertTrue(parser.parseLine("; foo = bar", builder));

		verify(builder);
	}
}
