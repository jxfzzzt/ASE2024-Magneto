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

package com.lithium.flow.jetty;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.util.Sleep;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Token<T> {
	private final CountDownLatch latch = new CountDownLatch(1);
	private final Decoder<T> decoder;
	private IOException exception;
	private T object;

	public Token(@Nonnull Decoder<T> decoder) {
		this.decoder = checkNotNull(decoder);
	}

	public void process(@Nonnull String result) {
		if (result.startsWith("ERROR: ")) {
			exception(new RuntimeException(result));
		} else {
			try {
				set(result);
			} catch (Exception e) {
				exception(e);
			}
		}
	}

	public void set(@Nonnull String result) throws IOException {
		object = decoder.decode(result);
		latch.countDown();
	}

	@Nonnull
	public T get(long timeout) throws IOException {
		if (!Sleep.softly(() -> latch.await(timeout, TimeUnit.MILLISECONDS))) {
			throw new IOException("interrupted");
		} else if (exception != null) {
			throw exception;
		} else if (object == null) {
			throw new IOException("timeout hit: " + timeout + "ms");
		} else {
			return object;
		}
	}

	public void exception(@Nonnull Exception e) {
		if (e instanceof IOException) {
			exception = (IOException) e;
		} else {
			exception = new IOException(e);
		}
		latch.countDown();
	}
}
