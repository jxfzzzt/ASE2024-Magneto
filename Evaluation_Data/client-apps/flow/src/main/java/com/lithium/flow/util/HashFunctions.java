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

import javax.annotation.Nonnull;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @author Matt Ayres
 */
public class HashFunctions {
	@Nonnull
	public static HashFunction of(@Nonnull String name) {
		checkNotNull(name);
		switch (name) {
			case "adler32":
				return Hashing.adler32();
			case "crc32":
				return Hashing.crc32();
			case "md5":
				return LegacyHashing.md5();
			case "sha1":
				return LegacyHashing.sha1();
			case "sha256":
				return Hashing.sha256();
			case "sha512":
				return Hashing.sha512();
			case "sipHash24":
				return Hashing.sipHash24();
			case "murmur3_32":
				return Hashing.murmur3_32();
			case "murmur3_128":
				return Hashing.murmur3_128();
			default:
				throw new RuntimeException("unknown hash: " + name);
		}
	}
}
