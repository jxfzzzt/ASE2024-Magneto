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

package com.lithium.flow.shell.tunnel;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Access;
import com.lithium.flow.access.Login;
import com.lithium.flow.config.Config;
import com.lithium.flow.io.Swallower;
import com.lithium.flow.shell.Shore;
import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.shell.Tunneler;
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
public class ShellTunneler implements Tunneler {
	private static final Logger log = Logs.getLogger();

	private final Config config;
	private final Access access;
	private final Shore shore;
	private final List<Tunnel> tunnels = Collections.synchronizedList(new ArrayList<>());

	public ShellTunneler(@Nonnull Config config, @Nonnull Access access, @Nonnull Shore shore) {
		this.config = checkNotNull(config);
		this.access = checkNotNull(access);
		this.shore = checkNotNull(shore);
	}

	@Nonnull
	public Tunnel getTunnel(@Nonnull String host, int port, @Nullable String through) throws IOException {
		List<Login> logins = new ArrayList<>();

		Login endLogin = access.getLogin(host);
		logins.add(endLogin);

		if (through != null) {
			logins.add(access.getLogin(through));
		}

		Optional<String> match = config.getMatch("shell.tunnels", endLogin.getHost());
		if (match.isPresent()) {
			for (String tunnel : Splitter.on(';').split(match.get())) {
				logins.add(access.getLogin(tunnel));
			}
		}

		Collections.reverse(logins);

		Tunnel tunnel = null;
		for (int i = 0; i < logins.size() - 1; i++) {
			Login thisLogin = logins.get(i);
			Login nextLogin = logins.get(i + 1);
			if (i == logins.size() - 2) {
				nextLogin = nextLogin.toBuilder().setPort(port).build();
			}

			log.debug("tunneling through {} to {}", thisLogin, nextLogin.getHostAndPort());

			Login login = access.getLogin(thisLogin.getHostAndPort());
			if (tunnel != null) {
				login = login.toBuilder().setHost("localhost").setPort(tunnel.getPort()).build();
			}
			tunnel = shore.getShell(login).tunnel(0, nextLogin.getHost(), nextLogin.getPortOrDefault(22));
		}

		if (tunnel != null) {
			tunnels.add(tunnel);
			return tunnel;
		} else {
			return new NoTunnel(host, port);
		}
	}

	@Override
	public void close() throws IOException {
		tunnels.forEach(Swallower::close);
		shore.close();
	}
}
