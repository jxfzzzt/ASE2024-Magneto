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
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toList;

import com.lithium.flow.util.Logs;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

/**
 * @author Matt Ayres
 */
public class ProcessCoder implements Coder {
	private final String extension;
	private final List<String> inCommands;
	private final List<String> outCommands;

	public ProcessCoder(@Nonnull String extension, @Nonnull List<String> inCommands,
			@Nonnull List<String> outCommands) {
		this.extension = checkNotNull(extension);
		this.inCommands = checkNotNull(inCommands);
		this.outCommands = checkNotNull(outCommands);
	}

	@Override
	@Nonnull
	public InputStream wrapIn(@Nonnull InputStream in) throws IOException {
		AtomicReference<IOException> exception = new AtomicReference<>();

		Process process = new ProcessBuilder(inCommands).start();
		runAsync(() -> {
			try (OutputStream out = process.getOutputStream()) {
				IOUtils.copy(in, out);
			} catch (IOException e) {
				exception.set(e);
			}
		});

		return new FilterInputStream(process.getInputStream()) {
			@Override
			public void close() throws IOException {
				super.close();
				ProcessCoder.this.close(process, null, exception.get());
			}
		};
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out, int option) throws IOException {
		CountDownLatch latch = new CountDownLatch(1);
		AtomicReference<IOException> exception = new AtomicReference<>();

		List<String> optionCommands = outCommands.stream()
				.map(c -> c.replace("{option}", String.valueOf(option)))
				.collect(toList());

		Process process = new ProcessBuilder(optionCommands).start();
		runAsync(() -> {
			try {
				try (InputStream in = process.getInputStream()) {
					IOUtils.copy(in, out);
					out.close();
				}
			} catch (IOException e) {
				exception.set(e);
			}
			latch.countDown();
		});

		return new FilterOutputStream(process.getOutputStream()) {
			@Override
			public void write(@Nonnull byte[] b, int off, int len) throws IOException {
				out.write(b, off, len);
			}

			@Override
			public void close() throws IOException {
				super.close();
				ProcessCoder.this.close(process, latch, exception.get());
			}
		};
	}

	private void close(@Nonnull Process process, @Nullable CountDownLatch latch, @Nullable IOException exception)
			throws IOException {
		try {
			process.waitFor();

			if (latch != null) {
				latch.await();
			}

			int exitValue = process.exitValue();
			if (exitValue != 0) {
				String errorMessage = IOUtils.toString(process.getErrorStream(), UTF_8);
				throw new IOException(Logs.message("got exit code {}: {}", exitValue, errorMessage));
			}

			if (exception != null) {
				throw exception;
			}
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}

	@Override
	@Nonnull
	public String getExtension() {
		return extension;
	}
}
