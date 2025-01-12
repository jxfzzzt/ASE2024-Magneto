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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import com.google.common.base.Throwables;

/**
 * @author Matt Ayres
 */
public class Lines {
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	@Nonnull
	public static Stream<String> stream(@Nonnull InputStream in) {
		checkNotNull(in);
		return stream(in, DEFAULT_CHARSET);
	}

	@Nonnull
	public static Stream<String> stream(@Nonnull InputStream in, @Nonnull Charset charset) {
		checkNotNull(in);
		checkNotNull(charset);

		Spliterator<String> spliterator = new IndefiniteSpliterator<String>() {
			private final LineIterator it = new LineIterator(new InputStreamReader(in, charset));

			@Override
			public boolean tryAdvance(Consumer<? super String> action) {
				try {
					if (it.hasNext()) {
						action.accept(it.nextLine());
						return true;
					} else {
						Unchecked.run(it::close);
						return false;
					}
				} catch (IllegalStateException e) {
					return false;
				}
			}
		};

		return StreamSupport.stream(spliterator, false);
	}

	public static void accept(@Nonnull InputStream in,
			@Nonnull CheckedConsumer<String, IOException> consumer) throws IOException {
		checkNotNull(in);
		checkNotNull(consumer);
		accept(in, DEFAULT_CHARSET, consumer);
	}

	public static void accept(@Nonnull InputStream in, @Nonnull Charset charset,
			@Nonnull CheckedConsumer<String, IOException> consumer) throws IOException {
		checkNotNull(in);
		checkNotNull(charset);
		checkNotNull(consumer);

		try (LineIterator it = IOUtils.lineIterator(in, charset)) {
			while (it.hasNext()) {
				consumer.accept(it.nextLine());
			}
		} catch (IllegalStateException e) {
			Throwables.throwIfInstanceOf(e.getCause(), IOException.class);
			throw new IOException(e);
		}
	}

	@Nonnull
	public static Iterable<String> iterate(@Nonnull InputStream in) throws IOException {
		checkNotNull(in);
		return iterate(in, DEFAULT_CHARSET);
	}

	@Nonnull
	public static Iterable<String> iterate(@Nonnull InputStream in, @Nonnull Charset charset) throws IOException {
		checkNotNull(in);
		checkNotNull(charset);

		LineIterator it = IOUtils.lineIterator(in, charset);
		AtomicBoolean used = new AtomicBoolean();

		return () -> {
			if (used.getAndSet(true)) {
				throw new RuntimeException("this iterable can only be used once");
			}

			return new Iterator<String>() {
				@Override
				public boolean hasNext() {
					boolean next = it.hasNext();
					if (!next) {
						Unchecked.run(it::close);
					}
					return next;
				}

				@Override
				public String next() {
					return it.nextLine();
				}
			};
		};
	}

	@Nonnull
	public static String first(@Nonnull InputStream in) throws IOException {
		checkNotNull(in);
		return first(in, DEFAULT_CHARSET);
	}

	@Nonnull
	public static String first(@Nonnull InputStream in, @Nonnull Charset charset) throws IOException {
		checkNotNull(in);

		LineIterator it = IOUtils.lineIterator(in, charset);
		String line = it.hasNext() ? it.nextLine() : "";
		it.close();

		return line;
	}
}
