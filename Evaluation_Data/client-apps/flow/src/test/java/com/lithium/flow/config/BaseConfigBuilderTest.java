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

package com.lithium.flow.config;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.lithium.flow.config.exception.IllegalConfigException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class BaseConfigBuilderTest {
	@Test
	public void testLoaders() throws IOException {
		ConfigLoader loader1 = createMock(ConfigLoader.class);
		ConfigLoader loader2 = createMock(ConfigLoader.class);
		expect(loader1.getInputStream("test.path")).andReturn(new ByteArrayInputStream("key = value".getBytes()));
		replay(loader1, loader2);

		new BaseConfigBuilder().resetLoaders().addLoader(loader1).addLoader(loader2).include("test.path");
		verify(loader1, loader2);
	}

	@Test
	public void testParsers() throws IOException {
		BaseConfigBuilder builder = new BaseConfigBuilder();
		ConfigParser parser1 = createMock(ConfigParser.class);
		expect(parser1.parseLine("key = value", builder)).andReturn(true);
		ConfigParser parser2 = createMock(ConfigParser.class);
		replay(parser1, parser2);

		builder.resetParsers().addParser(parser1).addParser(parser2).parseLine("key = value");
		verify(parser1, parser2);
	}

	@Test
	public void testParsersNotUsed() throws IOException {
		Config config = new BaseConfigBuilder().parseLine("key : value").build();
		assertEquals(map(), config.asRawMap());
	}

	@Test(expected = FileNotFoundException.class)
	public void testDefaultFileNotFound() throws IOException {
		new BaseConfigBuilder().include("/lithium/config/no.config");
	}

	@Test
	public void testAllowFileNotFound() throws IOException {
		new BaseConfigBuilder().allowFileNotFound(true).include("/lithium/config/no.config");
	}

	@Test(expected = IllegalConfigException.class)
	public void testDefaultRawValue() {
		new BaseConfigBuilder().build().getString("key");
	}

	@Test
	public void testAllowUndefined() {
		Config config = new BaseConfigBuilder().allowUndefined(true).build();
		assertNull(config.getString("key"));
	}

	@Test
	public void testDefaultRequiredKey() {
		new BaseConfigBuilder().requireKey("key").build();
	}

	@Test(expected = IllegalConfigException.class)
	public void testAllowRequiredKey() {
		new BaseConfigBuilder().allowRequiredKeys(true).requireKey("key").setString("key", "value").build();
		new BaseConfigBuilder().allowRequiredKeys(true).requireKey("key").build();
	}

	@Test
	public void testDefaults() {
		Config defaults = createMock(Config.class);
		Config config = new BaseConfigBuilder(defaults).build();
		expect(defaults.getValue("key", null, false, config, null)).andReturn("value");
		replay(defaults);
		assertEquals("value", config.getString("key"));
		verify(defaults);
	}

	@Test
	public void testSetString() {
		Config config = new BaseConfigBuilder().setString("key", "value").build();
		assertEquals("value", config.getString("key"));
	}

	@Test
	public void testSetAll() {
		Config config = new BaseConfigBuilder().setAll(map("key1", "value1", "key2", "value2")).build();
		assertEquals("value1", config.getString("key1"));
		assertEquals("value2", config.getString("key2"));
	}

	@Test
	public void testAddAll() {
		Config addConfig = createMock(Config.class);
		expect(addConfig.asRawMap()).andReturn(map("key1", "value1", "key2", "value2"));
		replay(addConfig);

		Config config = new BaseConfigBuilder().addAll(addConfig).build();
		assertEquals("value1", config.getString("key1"));
		assertEquals("value2", config.getString("key2"));
		verify(addConfig);
	}

	@Test
	public void testRemoveKey() {
		assertNull(new BaseConfigBuilder().setString("key", "value").removeKey("key").getString("key"));
	}

	@Test
	public void testGetString() {
		assertEquals("value", new BaseConfigBuilder().setString("key", "value").getString("key"));
	}

	@Test
	public void testGetStringWithDefaults() {
		Config defaults = createMock(Config.class);
		expect(defaults.getString("key")).andReturn("value");
		replay(defaults);
		assertEquals("value", new BaseConfigBuilder(defaults).getString("key"));
		verify(defaults);
	}

	@Test
	public void testInclude() throws IOException {
		Config config = new BaseConfigBuilder().include("/com/lithium/flow/config/test.config").build();
		assertEquals("value", config.getString("key"));
	}

	@Test(expected = IOException.class)
	public void testIncludeRecursion() throws IOException {
		new BaseConfigBuilder().include("/com/lithium/flow/config/recursive1.config").build();
	}

	@Test
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void testLoaderParser() throws IOException {
		File testDir = new File("target/test");
		File file = new File(testDir, "test.config");
		try {
			testDir.mkdirs();

			PrintStream ps = new PrintStream(new FileOutputStream(file));
			ps.println("key = value");
			ps.close();

			Config config = new BaseConfigBuilder().parseLine("!loader target/test").include("test.config").build();
			assertEquals("value", config.getString("key"));
		} finally {
			if (file.exists()) {
				file.delete();
			}
			if (testDir.exists()) {
				testDir.delete();
			}
		}
	}

	@Test
	public void testParseLine() throws IOException {
		BaseConfigBuilder builder = new BaseConfigBuilder();
		builder.parseLine("foo = value1");
		builder.pushPrefix("arf");
		builder.parseLine("foo = value2");
		builder.popPrefix();
		builder.parseLine("bar = value3");

		Config config = builder.build();
		assertEquals("value1", config.getString("foo"));
		assertEquals("value2", config.getString("arf.foo"));
		assertEquals("value3", config.getString("bar"));
	}

	@Test
	public void testPrefix() throws IOException {
		assertEquals("value", new BaseConfigBuilder().parseLine("key = value").build().getString("key"));
	}

	private Map<String, String> map(String... keyValues) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < keyValues.length; i += 2) {
			map.put(keyValues[i], keyValues[i + 1]);
		}
		return map;
	}
}
