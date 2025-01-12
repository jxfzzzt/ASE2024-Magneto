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

package com.lithium.flow.shell.sshj;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.schmizz.sshj.connection.channel.direct.Session.Command;

import com.lithium.flow.io.Swallower;
import com.lithium.flow.shell.Exec;
import com.lithium.flow.util.Lines;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import com.google.common.io.ByteStreams;

import net.schmizz.sshj.connection.channel.direct.Session;

/**
 * @author Matt Ayres
 */
public class SshjExec implements Exec {
	private final Command command;
	private final boolean pty;

	public SshjExec(@Nonnull Sshj ssh, @Nonnull String command, @Nullable Boolean pty) throws IOException {
		checkNotNull(ssh);
		checkNotNull(command);

		Session session = ssh.startSession();
		this.pty = pty != null ? pty : ssh.isPty();
		if (this.pty) {
			session.allocateDefaultPTY();
		}

		this.command = session.exec(command);
	}

	@Override
	@Nonnull
	public Stream<String> out() {
		return Lines.stream(command.getInputStream());
	}

	@Override
	@Nonnull
	public Stream<String> err() {
		return Lines.stream(command.getErrorStream());
	}

	@Override
	@Nonnull
	public Optional<Integer> exit() throws IOException {
		IOUtils.copy(command.getInputStream(), ByteStreams.nullOutputStream());
		IOUtils.copy(command.getErrorStream(), ByteStreams.nullOutputStream());
		close();
		return Optional.ofNullable(command.getExitStatus());
	}

	@Override
	@Nonnull
	public InputStream getInputStream() {
		return command.getInputStream();
	}

	@Override
	@Nonnull
	public InputStream getErrorStream() {
		return command.getErrorStream();
	}

	@Override
	@Nonnull
	public OutputStream getOutputStream() {
		return command.getOutputStream();
	}

	@Override
	public void close() throws IOException {
		if (pty) {
			command.close();
		} else {
			Swallower.close(command.getOutputStream());
			Swallower.close(command.getInputStream());
			Swallower.close(command.getErrorStream());
		}
	}
}
