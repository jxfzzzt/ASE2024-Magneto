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

package com.lithium.flow.shell;

import com.lithium.flow.filer.Filer;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Joiner;

/**
 * @author Matt Ayres
 */
public interface Shell extends Closeable {
	@Nonnull
	URI getUri();

	@Nonnull
	default Exec exec(@Nonnull String command) throws IOException {
		return exec(command, null);
	}

	@Nonnull
	Exec exec(@Nonnull String command, @Nullable Boolean pty) throws IOException;

	@Nonnull
	default Exec exec(@Nonnull List<String> commands) throws IOException {
		return exec(commands, null);
	}

	@Nonnull
	default Exec exec(@Nonnull List<String> commands, @Nullable Boolean pty) throws IOException {
		return exec(Joiner.on('\n').join(commands), pty);
	}

	@Nonnull
	Tunnel tunnel(@Nonnull Tunneling tunneling) throws IOException;

	@Nonnull
	default Tunnel tunnel(int listen, @Nonnull String host, int port) throws IOException {
		return tunnel(new Tunneling(listen, host, port));
	}

	@Nonnull
	Filer getFiler() throws IOException;
}
