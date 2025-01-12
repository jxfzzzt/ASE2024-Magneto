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

import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.shell.Tunneling;
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import net.schmizz.sshj.connection.channel.direct.LocalPortForwarder;
import net.schmizz.sshj.connection.channel.direct.Parameters;

/**
 * @author Matt Ayres
 */
public class SshjTunnel extends Thread implements Tunnel {
	private static final Logger log = Logs.getLogger();

	private final int port;
	private final LocalPortForwarder forwarder;
	private final AtomicBoolean closed = new AtomicBoolean();

	public SshjTunnel(@Nonnull Sshj ssh, @Nonnull Tunneling tunneling) throws IOException {
		checkNotNull(ssh);
		checkNotNull(tunneling);

		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(getHost(), tunneling.getListen()));
		port = server.getLocalPort();

		Parameters parameters = new Parameters(getHost(), getPort(), tunneling.getHost(), tunneling.getPort());
		forwarder = ssh.newLocalPortForwarder(parameters, server);

		start();
	}

	@Override
	public void run() {
		log.debug("tunnel listening on port {}", port);

		while (!closed.get()) {
			try {
				forwarder.listen();
			} catch (IOException e) {
				log.debug("tunnel failed", e);
			}
		}

		log.debug("tunnel closed on port {}", port);
	}

	@Override
	@Nonnull
	public String getHost() {
		return "localhost";
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void close() throws IOException {
		closed.set(true);
		forwarder.close();
	}
}
