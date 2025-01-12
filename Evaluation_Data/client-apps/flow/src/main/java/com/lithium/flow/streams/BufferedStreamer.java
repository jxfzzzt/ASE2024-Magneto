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

import com.lithium.flow.config.Config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;

/**
 * Filter to apply buffering for output and input.
 * 
 * @author Matt Ayres
 */
public final class BufferedStreamer implements Streamer {
	private final int size;

	public BufferedStreamer(@Nonnull Config config) {
		checkNotNull(config);
		size = config.getInt("buffer.size", 65536);
	}

	@Override
	@Nonnull
	public final OutputStream filterOut(@Nonnull OutputStream out, String name) {
		return new BufferedOutputStream(out, size);
	}

	@Override
	@Nonnull
	public final InputStream filterIn(@Nonnull InputStream in, String name) {
		return new BufferedInputStream(in, size);
	}
}
