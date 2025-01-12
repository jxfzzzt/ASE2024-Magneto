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

package com.lithium.flow.streams;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.compress.Coder;
import com.lithium.flow.compress.Coders;
import com.lithium.flow.compress.ParallelCoder;
import com.lithium.flow.config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Filter to apply compression on output and decompression on input.
 *
 * @author Matt Ayres
 */
public final class CompressStreamer implements Streamer {
	private final Coder inCoder;
	private final Coder outCoder;
	private final int compressOption;

	public CompressStreamer(@Nonnull Config config) {
		checkNotNull(config);
		inCoder = getCoder(config.prefix("in"));
		outCoder = getCoder(config.prefix("out"));
		compressOption = config.getInt("compress.option", -1);
	}

	@Nonnull
	private static Coder getCoder(@Nonnull Config config) {
		Coder coder = Coders.getCoder(config.getString("compress.type", ""));
		if (config.getBoolean("compress.parallel", false)) {
			coder = new ParallelCoder(coder);
		}
		return coder;
	}

	@Override
	@Nonnull
	public final OutputStream filterOut(@Nonnull OutputStream out, @Nullable String name) throws IOException {
		return outCoder.wrapOut(out, compressOption);
	}

	@Override
	@Nonnull
	public final InputStream filterIn(@Nonnull InputStream in, @Nullable String name) throws IOException {
		return inCoder.wrapIn(in);
	}
}
