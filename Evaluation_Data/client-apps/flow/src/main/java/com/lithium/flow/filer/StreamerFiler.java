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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.streams.Streamer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;

/**
 * Decorates an instance of {@link Filer} to manipulate the reading and writing of streams.
 *
 * @author Matt Ayres
 */
public class StreamerFiler extends DecoratedFiler {
	private final Streamer streamer;

	public StreamerFiler(@Nonnull Filer delegate, @Nonnull Streamer streamer) {
		super(checkNotNull(delegate));
		this.streamer = checkNotNull(streamer);

		bypassDelegateHash = true;
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		final InputStream in = super.readFile(path);
		try {
			return streamer.filterIn(in, path);
		} catch (Exception e) {
			return new InputStream() {
				@Override
				public int read() throws IOException {
					throw new IOException(e);
				}

				@Override
				public void close() throws IOException {
					in.close();
				}
			};
		}
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		OutputStream out = super.writeFile(path);
		try {
			return streamer.filterOut(out, path);
		} catch (Exception e) {
			return exceptionOut(out, e);
		}
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		OutputStream out = super.appendFile(path);
		try {
			return streamer.filterOut(out, path);
		} catch (Exception e) {
			return exceptionOut(out, e);
		}
	}

	@Nonnull
	private OutputStream exceptionOut(@Nonnull OutputStream out, Exception e) {
		return new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				throw new IOException(e);
			}

			@Override
			public void close() throws IOException {
				out.close();
			}
		};
	}
}
