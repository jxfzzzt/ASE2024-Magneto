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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class FileConfigLoaderTest {
	private static File file;

	@BeforeClass
	public static void setUp() throws Exception {
		file = File.createTempFile("test", ".config");
		file.deleteOnExit();
		PrintStream ps = new PrintStream(file.getAbsolutePath());
		ps.println("foo = bar");
		ps.close();
	}

	@Test
	public void testAbsolutePath() throws IOException {
		InputStream in = new FileConfigLoader().getInputStream(file.getAbsolutePath());
		assertNotNull(in);
		in.close();
	}

	@Test
	public void testRelativePath() throws IOException {
		InputStream in = new FileConfigLoader(file.getParent()).getInputStream(file.getName());
		assertNotNull(in);
		in.close();
	}

	@Test
	public void testInvalidPath() throws IOException {
		InputStream in = new FileConfigLoader().getInputStream("/foo/bar/test.config");
		assertNull(in);
	}
}
