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
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.lithium.flow.config.ConfigBuilder;
import com.lithium.flow.config.loaders.FileConfigLoader;

import org.easymock.EasyMock;
import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class LoaderConfigParserTest {
	@Test
	public void testLoader() {
		testLoader("!loader dir1", "dir1");
		testLoader("!loader\tdir1/dir2", "dir1/dir2");
		testLoader("!loader  dir1/dir2/dir3", "dir1/dir2/dir3");
		testLoader("!loader dir1 ", "dir1");
	}

	private void testLoader(String line, String loader) {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		LoaderConfigParser.Callback callback = createMock(LoaderConfigParser.Callback.class);
		callback.onLoader(EasyMock.isA(FileConfigLoader.class));
		expectLastCall();
		replay(builder, callback);
		assertTrue(new LoaderConfigParser(callback).parseLine(line, builder));
		verify(builder, callback);
	}

	@Test
	public void testNoLoader() {
		testNoLoader("#loader dir1");
		testNoLoader(" !loader dir1/dir2");
	}

	private void testNoLoader(String line) {
		ConfigBuilder builder = createMock(ConfigBuilder.class);
		LoaderConfigParser.Callback callback = createMock(LoaderConfigParser.Callback.class);
		replay(builder);
		assertFalse(new LoaderConfigParser(callback).parseLine(line, builder));
		verify(builder);
	}
}
