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

import java.util.Iterator;

import javax.annotation.Nonnull;

import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;

/**
 * @author Matt Ayres
 */
public class BaseEncodings {
	@Nonnull
	public static BaseEncoding of(@Nonnull String name) {
		checkNotNull(name);

		Iterator<String> it = Splitter.on(".").split(name).iterator();
		BaseEncoding encoding = getEncoding(it.next());
		while (it.hasNext()) {
			String mod = it.next();
			switch (mod) {
				case "lowerCase":
					encoding = encoding.lowerCase();
					break;
				case "upperCase":
					encoding = encoding.upperCase();
					break;
				case "omitPadding":
					encoding = encoding.omitPadding();
					break;
				default:
					throw new RuntimeException("unknown modifier: " + mod);
			}
		}
		return encoding;
	}

	@Nonnull
	private static BaseEncoding getEncoding(@Nonnull String name) {
		switch (name) {
			case "base16":
				return BaseEncoding.base16();
			case "base32":
				return BaseEncoding.base32();
			case "base32Hex":
				return BaseEncoding.base32Hex();
			case "base64":
				return BaseEncoding.base64();
			case "base64Url":
				return BaseEncoding.base64Url();
			default:
				throw new RuntimeException("unknown encoding: " + name);
		}
	}
}
