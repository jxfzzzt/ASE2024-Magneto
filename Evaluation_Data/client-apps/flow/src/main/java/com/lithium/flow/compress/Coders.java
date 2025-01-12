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

package com.lithium.flow.compress;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Coders {
	private static final CoderFactory FACTORY = buildFactory();

	@Nonnull
	public static CoderFactory buildFactory() {
		return new CoderFactory()
				.register(".bz2", Bzip2Coder.class)
				.register(".gz", GzipCoder.class)
				.register(".lz4", Lz4Coder.class)
				.register(".lzf", LzfCoder.class)
				.register(".lzo", LzopCoder.class)
				.register(".lzo_deflate", LzoCoder.class)
				.register(".snappy", SnappyCoder.class)
				.register(".xz", XzCoder.class);
	}

	@Nonnull
	public static CoderFactory getFactory() {
		return FACTORY;
	}

	@Nonnull
	public static Coder getCoder(@Nonnull String path) {
		checkNotNull(path);
		return FACTORY.getCoder(path);
	}
}
