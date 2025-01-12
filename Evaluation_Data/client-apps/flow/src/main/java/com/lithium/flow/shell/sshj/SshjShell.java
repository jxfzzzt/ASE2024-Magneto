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

import com.lithium.flow.filer.Filer;
import com.lithium.flow.shell.Exec;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.shell.Tunneling;
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.net.URI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class SshjShell implements Shell {
	private static final Logger log = Logs.getLogger();

	private final Sshj ssh;
	private final URI uri;

	public SshjShell(@Nonnull Sshj ssh, @Nonnull URI uri) {
		this.ssh = checkNotNull(ssh);
		this.uri = checkNotNull(uri);
	}

	@Override
	@Nonnull
	public URI getUri() {
		return uri;
	}

	@Override
	@Nonnull
	public synchronized Exec exec(@Nonnull String command, @Nullable Boolean pty) throws IOException {
		checkNotNull(command);
		return new SshjExec(ssh, command, pty);
	}

	@Override
	@Nonnull
	public Tunnel tunnel(@Nonnull Tunneling tunneling) throws IOException {
		checkNotNull(tunneling);
		return new SshjTunnel(ssh, tunneling);
	}

	@Override
	@Nonnull
	public synchronized Filer getFiler() throws IOException {
		return new SshjFiler(ssh, uri);
	}

	@Override
	public void close() throws IOException {
		log.debug("close: {}", uri);
		ssh.close();
	}
}
