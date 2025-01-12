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

package com.lithium.flow.config.loaders;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class ClasspathConfigLoaderTest {
	@Test
	public void testAbsolutePath() throws IOException {
		InputStream in = new ClasspathConfigLoader().getInputStream("/com/lithium/flow/config/test.config");
		assertNotNull(in);
		in.close();
	}

	@Test
	public void testRelativePath() throws IOException {
		InputStream in = new ClasspathConfigLoader("/com/lithium/flow/config").getInputStream("test.config");
		assertNotNull(in);
		in.close();
	}

	@Test
	public void testInvalidPath() {
		InputStream in = new ClasspathConfigLoader("/foo/bar").getInputStream("test.config");
		assertNull(in);
	}
}
