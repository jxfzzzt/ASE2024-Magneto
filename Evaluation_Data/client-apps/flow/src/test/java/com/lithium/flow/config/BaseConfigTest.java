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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.lithium.flow.config.exception.IllegalConfigException;
import com.lithium.flow.store.MemoryStore;
import com.lithium.flow.store.Store;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author Matt Ayres
 */
public class BaseConfigTest {
	@Test
	public void testWrapperBuilder() {
		BaseConfig config = new BaseConfig("test", store("key", "value"));
		assertEquals(toMap(config), toMap(config.toBuilder().build()));
	}

	@Test
	public void testContainsKey() {
		BaseConfig config = new BaseConfig("test", store("key", "value"));
		assertTrue(config.containsKey("key"));
		assertFalse(config.containsKey("no.key"));
	}

	@Test
	public void testContainsKeyWithDefaults() {
		BaseConfig defaults = new BaseConfig("test", store("key", "value"));
		BaseConfig config = new BaseConfig("test", store(), defaults, false);
		assertTrue(config.containsKey("key"));
		assertFalse(config.containsKey("no.key"));
	}

	@Test
	public void testRaw() {
		BaseConfig config = new BaseConfig("test", store("key", "${foo}", "foo", "value"));
		assertEquals("${foo}", config.getRaw("key"));
	}

	@Test
	public void testRawWithDefaults() {
		BaseConfig defaults = new BaseConfig("test", store("key", "${foo}", "foo", "value"));
		BaseConfig config = new BaseConfig("test", store(), defaults, false);
		assertEquals("${foo}", config.getRaw("key"));
	}

	@Test
	public void testGetRawMap() {
		Map<String, String> map = map("key", "${foo}", "foo", "value");
		BaseConfig config = new BaseConfig("test", store(map));
		assertEquals(map, config.asRawMap());
	}

	@Test
	public void testAsMap() {
		Store rawMap = store("key", "${foo}", "foo", "value");
		Map<String, String> map = map("key", "value", "foo", "value");
		BaseConfig config = new BaseConfig("test", rawMap);
		assertEquals(map, config.asMap());
	}

	@Test
	public void testGetRawMapWithDefaults() {
		Map<String, String> map = map("key", "${foo}", "foo", "value");
		Store map1 = store("key", "${foo}");
		Store map2 = store("foo", "value");
		BaseConfig defaults = new BaseConfig("test", map1);
		BaseConfig config = new BaseConfig("test", map2, defaults, false);
		assertEquals(map, config.asRawMap());
	}

	@Test
	public void testGetString() {
		BaseConfig config = new BaseConfig("test", store("key", "value"));
		assertEquals("value", config.getString("key"));
		assertEquals("value", config.getString("key", ""));
		assertEquals("value", config.getString("key", null));
		assertEquals("value2", config.getString("no.key", "value2"));
		assertNull(config.getString("no.key", null));
	}

	@Test
	public void testGetStringWithDefaults() {
		BaseConfig defaults = new BaseConfig("test", store("key", "value"));
		BaseConfig config = new BaseConfig("test", store(), defaults, false);
		assertEquals("value", config.getString("key"));
		assertEquals("value", config.getString("key", ""));
		assertEquals("value", config.getString("key", null));
	}

	@Test(expected = IllegalConfigException.class)
	public void testGetStringRawValue() {
		new BaseConfig("test", store()).getString("arf");
	}

	@Test
	public void testGetStringRawValueAllowed() {
		BaseConfig config = new BaseConfig("test", store(), null, true);
		assertNull(config.getString("key"));
	}

	@Test
	public void testGetInt() {
		BaseConfig config = new BaseConfig("test", store("key", "34"));
		assertEquals(34, config.getInt("key"));
		assertEquals(34, config.getInt("key", -1));
		assertEquals(34, config.getInt("key", 34));
		assertEquals(55, config.getInt("no.key", 55));
	}

	@Test
	public void testGetIntWithDefaults() {
		BaseConfig defaults = new BaseConfig("test", store("key", "34"));
		BaseConfig config = new BaseConfig("test", store(), defaults, false);
		assertEquals(34, config.getInt("key"));
		assertEquals(34, config.getInt("key", -1));
		assertEquals(55, config.getInt("no.key", 55));
	}

	@Test(expected = IllegalConfigException.class)
	public void testGetIntIllegal() {
		new BaseConfig("test", store("key", "arf")).getInt("key");
	}

	@Test
	public void testGetLong() {
		BaseConfig config = new BaseConfig("test", store("key", "17179869184"));
		assertEquals(17179869184L, config.getLong("key"));
		assertEquals(17179869184L, config.getLong("key", -1));
		assertEquals(17179869184L, config.getLong("key", 17179869184L));
		assertEquals(34349869184L, config.getLong("no.key", 34349869184L));
	}

	@Test
	public void testGetLongWithDefaults() {
		BaseConfig defaults = new BaseConfig("test", store("key", "17179869184"));
		BaseConfig config = new BaseConfig("test", store(), defaults, false);
		assertEquals(17179869184L, config.getLong("key"));
		assertEquals(17179869184L, config.getLong("key", -1));
		assertEquals(34349869184L, config.getLong("no.key", 34349869184L));
	}

	@Test(expected = IllegalConfigException.class)
	public void testGetLongIllegal() {
		new BaseConfig("test", store("key", "arf")).getLong("key");
	}

	@Test
	public void testGetTime() {
		BaseConfig config = new BaseConfig("test", store("key", "34m", "key2", "50d"));
		assertEquals(34 * 60000L, config.getTime("key"));
		assertEquals(34 * 60000L, config.getTime("key", "0m"));
		assertEquals(34 * 60000L, config.getTime("key", "34m"));
		assertEquals(55 * 60000L, config.getTime("no.key", "55m"));
		assertEquals(50 * 1440 * 60000L, config.getTime("key2"));
	}

	@Test(expected = IllegalConfigException.class)
	public void testGetTimeIllegal() {
		new BaseConfig("test", store("key", "arf")).getTime("key");
	}

	@Test
	public void testGetDouble() {
		BaseConfig config = new BaseConfig("test", store("key", "34.34"));
		assertEquals(34.34, config.getDouble("key"), 0.0);
		assertEquals(34.34, config.getDouble("key", -1), 0.0);
		assertEquals(34.34, config.getDouble("key", 34.34), 0.0);
		assertEquals(55.55, config.getDouble("no.key", 55.55), 0.0);
	}

	@Test(expected = IllegalConfigException.class)
	public void testGetDoubleIllegal() {
		new BaseConfig("test", store("key", "arf")).getDouble("key");
	}

	@Test
	public void testGetBoolean() {
		BaseConfig config = new BaseConfig("test", store("key.true", "true", "key.false", "false"));
		assertTrue(config.getBoolean("key.true"));
		assertTrue(config.getBoolean("key.true", false));
		assertFalse(config.getBoolean("key.false"));
		assertFalse(config.getBoolean("key.false", true));
		assertTrue(config.getBoolean("no.key", true));
		assertFalse(config.getBoolean("no.key", false));
	}

	@Test(expected = IllegalConfigException.class)
	public void testGetBooleanInvalid() {
		new BaseConfig("test", store("key", "arf")).getBoolean("key");
	}

	@Test(expected = IllegalConfigException.class)
	public void testGetBooleanRawValue() {
		new BaseConfig("test", store()).getBoolean("key");
	}

	@Test
	public void testGetBooleanRawValueAllowed() {
		assertFalse(new BaseConfig("test", store(), null, true).getBoolean("key"));
	}

	@Test
	public void testGetList() {
		BaseConfig config = new BaseConfig("test", store("key", "value1 value2"));
		assertEquals(list("value1", "value2"), config.getList("key"));
		assertEquals(list("value1", "value2"), config.getList("key", Configs.emptyList()));
		assertEquals(list("value3", "value4"), config.getList("no.key", Arrays.asList("value3", "value4")));
	}

	@Test
	public void testInterpolation() {
		Map<String, String> map = new HashMap<>();
		map.put("key", "${foo}");
		map.put("foo", "${bar}");
		map.put("bar", "value");
		map.put("baz", "${foo} ${bar}");
		map.put("key2", "${foo2}");
		BaseConfig config = new BaseConfig("test", store(map));
		assertEquals("value", config.getString("key"));
		assertEquals("value", config.getString("foo"));
		assertEquals("value", config.getString("bar"));
		assertEquals("value value", config.getString("baz"));
	}

	@Test(expected = IllegalConfigException.class)
	public void testInterpolationMissing() {
		new BaseConfig("test", store("key", "${key2}")).getString("key");
	}

	@Test(expected = IllegalConfigException.class)
	public void testInterpolationMissing2() {
		new BaseConfig("test", store("key", "x ${key2} y ${key3} z", "key2", "value")).getString("key");
	}

	@Test(expected = IllegalConfigException.class)
	public void testInterpolationMissingNested() {
		new BaseConfig("test", store("key", "${key2}", "key2", "${key3}")).getString("key");
	}

	@Test
	public void testInterpolationMissingAllowUndefined() {
		assertEquals("${key2}", new BaseConfig("test", store("key", "${key2}"), null, true).getString("key"));
	}

	@Test(expected = IllegalConfigException.class)
	public void testRecursion() {
		new BaseConfig("test", store("key", "${key}value")).getString("key");
	}

	@Test(expected = IllegalConfigException.class)
	public void testIndirectRecursion() {
		new BaseConfig("test", store("key", "${key}value", "key2", "${key}value")).getString("key2");
	}

	@Test(expected = IllegalConfigException.class)
	public void testInfiniteRecursion() {
		new BaseConfig("test", store("key", "${key2}value", "key2", "${key}value")).getString("key");
	}

	private List<String> list(String... values) {
		return Lists.newArrayList(values);
	}

	private Store store(String... keyValues) {
		return new MemoryStore(map(keyValues));
	}

	private Store store(Map<String, String> map) {
		return new MemoryStore(map);
	}

	private Map<String, String> map(String... keyValues) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < keyValues.length; i += 2) {
			map.put(keyValues[i], keyValues[i + 1]);
		}
		return map;
	}

	private Map<String, String> toMap(Config config) {
		Map<String, String> map = new HashMap<>();
		for (String key : config.asRawMap().keySet()) {
			map.put(key, config.getString(key));
		}
		return map;
	}
}
