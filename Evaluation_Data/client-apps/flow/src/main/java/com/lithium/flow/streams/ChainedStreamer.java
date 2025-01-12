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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

/**
 * Chains a list of {@link Streamer} implementations together. Filters are layered on top of each other in the
 * ordered specified. The last filter is thus run first for both input and output streams.
 * 
 * @author Matt Ayres
 */
public class ChainedStreamer implements Streamer {
	private final ImmutableList<Streamer> streamers;

	public ChainedStreamer(@Nonnull List<Streamer> streamers) {
		this.streamers = ImmutableList.copyOf(checkNotNull(streamers));
	}

	@Override
	@Nonnull
	public OutputStream filterOut(@Nonnull OutputStream out, String name) throws IOException {
		for (Streamer streamer : streamers) {
			out = streamer.filterOut(out, name);
		}
		return out;
	}

	@Override
	@Nonnull
	public InputStream filterIn(@Nonnull InputStream in, String name) throws IOException {
		for (Streamer filter : streamers) {
			in = filter.filterIn(in, name);
		}
		return in;
	}
}
