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

import com.lithium.flow.util.Sleep;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import com.google.common.collect.Queues;

/**
 * @author Matt Ayres
 */
public class ParallelCoder implements Coder {
	private final Coder delegate;
	private final int chunkSize;
	private final int threads;

	public ParallelCoder(@Nonnull Coder delegate) {
		this(delegate, 4 * 1024 * 1024, Runtime.getRuntime().availableProcessors());
	}

	public ParallelCoder(@Nonnull Coder delegate, int chunkSize, int threads) {
		this.delegate = checkNotNull(delegate);
		this.chunkSize = chunkSize;
		this.threads = threads;
	}

	@Override
	@Nonnull
	public InputStream wrapIn(@Nonnull InputStream in) {
		return in;
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out, int option) {
		return new FilterOutputStream(out) {
			private ByteArrayOutputStream buf = new ByteArrayOutputStream();
			private volatile boolean running;
			private BlockingQueue<Future<byte[]>> queue;
			private ExecutorService service;
			private Thread thread;
			private Exception exception;

			@Override
			public void write(int b) {
				buf.write(b);
				if (buf.size() > chunkSize) {
					cycle();
				}
			}

			@Override
			public void write(@Nonnull byte[] b) throws IOException {
				buf.write(b);
				if (buf.size() > chunkSize) {
					cycle();
				}
			}

			@Override
			public void write(@Nonnull byte[] b, int off, int len) {
				buf.write(b, off, len);
				if (buf.size() > chunkSize) {
					cycle();
				}
			}

			@Override
			public void flush() throws IOException {
				cycle();
				running = false;
				service.shutdown();
				Sleep.softly(thread::join);
				if (exception != null) {
					throw new IOException(exception);
				}

				out.flush();
			}

			private void cycle() {
				if (!running) {
					running = true;
					queue = Queues.newLinkedBlockingQueue();
					service = Executors.newFixedThreadPool(threads);
					thread = new Thread(this::run);
					thread.start();
				}

				byte[] bytes = buf.toByteArray();
				buf = new ByteArrayOutputStream();

				queue.add(service.submit(() -> {
					ByteArrayOutputStream compressBuf = new ByteArrayOutputStream();
					OutputStream compressOut = delegate.wrapOut(compressBuf, option);
					compressOut.write(bytes);
					compressOut.close();
					return compressBuf.toByteArray();
				}));
			}

			private void run() {
				while (running || queue.size() > 0) {
					try {
						out.write(queue.take().get());
					} catch (Exception e) {
						exception = e;
						break;
					}
				}
			}
		};
	}

	@Override
	@Nonnull
	public String getExtension() {
		return delegate.getExtension();
	}
}
