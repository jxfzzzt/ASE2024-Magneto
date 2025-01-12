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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.io.BaseEncoding;

/**
 * @author Matt Ayres
 */
public class Encodings {
	private static final Map<String, BaseEncoding> map = new HashMap<>();

	static {
		map.put("base16", BaseEncoding.base16());
		map.put("base32", BaseEncoding.base32());
		map.put("base32Hex", BaseEncoding.base32Hex());
		map.put("base64", BaseEncoding.base64());
		map.put("base64Url", BaseEncoding.base64Url());
	}

	@Nonnull
	public static BaseEncoding get(@Nonnull String name) {
		checkNotNull(name);
		BaseEncoding encoding = map.get(name);
		if (encoding == null) {
			throw new IllegalArgumentException("illegal encoding: " + name);
		}
		return encoding;
	}
}
