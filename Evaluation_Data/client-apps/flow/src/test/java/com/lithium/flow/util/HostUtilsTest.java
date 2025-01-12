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

package com.lithium.flow.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author Matt Ayres
 */
public class HostUtilsTest {
	@Test
	public void testNone() {
		List<String> expected = Collections.singletonList("http://foo.li");
		List<String> expanded = HostUtils.expand("http://foo.li");
		assertEquals(expected, expanded);
	}

	@Test
	public void testExpand() {
		List<String> expected = Arrays.asList("http://foo8.li", "http://foo9.li", "http://foo10.li");
		List<String> expanded = HostUtils.expand("http://foo[8-10].li");
		assertEquals(expected, expanded);
	}

	@Test
	public void testExpandPadded() {
		List<String> expected = Arrays.asList("http://foo08.li", "http://foo09.li", "http://foo10.li");
		List<String> expanded = HostUtils.expand("http://foo[08-10].li");
		assertEquals(expected, expanded);
	}

	@Test
	public void testExpandCommas() {
		List<String> expected = Arrays.asList("http://foo7.li", "http://foo77.li", "http://foo777.li");
		List<String> expanded = HostUtils.expand("http://foo[7,77,777].li");
		assertEquals(expected, expanded);
	}

	@Test
	public void testExpandList() {
		List<String> expected = Arrays.asList("foo1", "foo5", "bar8", "bar9", "bar10", "baz");
		List<String> expanded = HostUtils.expand(Arrays.asList("foo[1,5]", "bar[8-10]", "baz"));
		assertEquals(expected, expanded);
	}

	@Test
	public void testExclude() {
		List<String> expected = Arrays.asList("foo1", "foo2", "foo4", "foo5");
		List<String> expanded = HostUtils.expand("foo[1-5,!3]");
		assertEquals(expected, expanded);
	}

	@Test
	public void testMultiple() {
		List<String> expected = Arrays.asList("foo13", "foo14", "foo23", "foo24");
		List<String> expanded = HostUtils.expand("foo[1-2][3-4]");
		assertEquals(expected, expanded);
	}
}
