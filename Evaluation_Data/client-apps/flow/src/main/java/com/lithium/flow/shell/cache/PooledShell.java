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

package com.lithium.flow.shell.cache;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Login;
import com.lithium.flow.config.Config;
import com.lithium.flow.filer.DecoratedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.io.Swallower;
import com.lithium.flow.shell.Exec;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.shell.Shore;
import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.shell.Tunneling;
import com.lithium.flow.util.CheckedLazy;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public class PooledShell implements Shell {
	private final int maxSessions;
	private final Shore shore;
	private final Login login;
	private final URI uri;
	private final Deque<Slot> slots = new ArrayDeque<>();

	public PooledShell(@Nonnull Config config, @Nonnull Shore shore, @Nonnull Login login) throws IOException {
		maxSessions = checkNotNull(config).getInt("shell.maxSessions", 9);
		this.shore = checkNotNull(shore);
		this.login = checkNotNull(login);
		try (Token token = createToken()) {
			uri = token.getShell().getUri();
		}
	}

	@Override
	@Nonnull
	public URI getUri() {
		return uri;
	}

	@Override
	@Nonnull
	public Exec exec(@Nonnull String command, @Nullable Boolean pty) throws IOException {
		checkNotNull(command);

		Token token = createToken();
		Exec exec = token.getShell().exec(command, pty);
		return new Exec() {
			@Nonnull
			@Override
			public Stream<String> out() {
				return exec.out();
			}

			@Nonnull
			@Override
			public Stream<String> err() {
				return exec.err();
			}

			@Nonnull
			@Override
			public Optional<Integer> exit() throws IOException {
				return exec.exit();
			}

			@Override
			@Nonnull
			public InputStream getInputStream() {
				return exec.getInputStream();
			}

			@Override
			@Nonnull
			public InputStream getErrorStream() {
				return exec.getErrorStream();
			}

			@Override
			@Nonnull
			public OutputStream getOutputStream() {
				return exec.getOutputStream();
			}

			@Override
			public void close() throws IOException {
				try {
					exec.close();
				} finally {
					token.close();
				}
			}
		};
	}

	@Override
	@Nonnull
	public Tunnel tunnel(@Nonnull Tunneling tunneling) throws IOException {
		checkNotNull(tunneling);

		try (Token token = createToken()) {
			return token.getShell().tunnel(tunneling);
		}
	}

	@Override
	@Nonnull
	public Filer getFiler() throws IOException {
		Token token = createToken();
		return new DecoratedFiler(token.getShell().getFiler()) {
			@Override
			public void close() throws IOException {
				try {
					super.close();
				} finally {
					token.close();
				}
			}
		};
	}

	@Override
	public void close() {
		synchronized (slots) {
			slots.forEach(Swallower::close);
		}
	}

	@Nonnull
	private Token createToken() {
		return new Token(findSlot());
	}

	@Nonnull
	private Slot findSlot() {
		synchronized (slots) {
			for (Slot slot : slots) {
				if (slot.reserve()) {
					return slot;
				}
			}

			Slot slot = new Slot();
			slot.reserve();
			slots.addFirst(slot);
			return slot;
		}
	}

	private class Token implements Closeable {
		private final Slot slot;
		private final AtomicBoolean closed = new AtomicBoolean();

		private Token(@Nonnull Slot slot) {
			this.slot = slot;
		}

		@Nonnull
		private Shell getShell() throws IOException {
			return slot.getShell();
		}

		@Override
		public void close() {
			if (!closed.getAndSet(true)) {
				slot.release();
			}
		}
	}

	private class Slot implements Closeable {
		private final Semaphore semaphore = new Semaphore(maxSessions);
		private final CheckedLazy<Shell, IOException> shell = new CheckedLazy<>(() -> shore.getShell(login));

		private boolean reserve() {
			return semaphore.tryAcquire();
		}

		private void release() {
			semaphore.release();
		}

		@Nonnull
		private Shell getShell() throws IOException {
			return shell.get();
		}

		@Override
		public void close() throws IOException {
			shell.get().close();
		}
	}
}
