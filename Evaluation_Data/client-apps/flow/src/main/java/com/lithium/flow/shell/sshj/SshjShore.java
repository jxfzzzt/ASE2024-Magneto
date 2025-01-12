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

import com.lithium.flow.access.Access;
import com.lithium.flow.access.Login;
import com.lithium.flow.config.Config;
import com.lithium.flow.io.Swallower;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.shell.Shore;
import com.lithium.flow.shell.util.DecoratedShell;
import com.lithium.flow.util.Mutex;
import com.lithium.flow.util.Mutexes;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class SshjShore implements Shore {
	private final Config config;
	private final Access access;
	private final Set<Shell> shells = Collections.synchronizedSet(new HashSet<>());
	private final Mutexes<String> mutexes = new Mutexes<>();

	public SshjShore(@Nonnull Config config, @Nonnull Access access) {
		this.config = checkNotNull(config);
		this.access = checkNotNull(access);
	}

	@Override
	@Nonnull
	public Shell getShell(@Nonnull String host) throws IOException {
		checkNotNull(host);
		return getShell(access.getLogin(host));
	}

	@Override
	@Nonnull
	public Shell getShell(@Nonnull Login login) throws IOException {
		checkNotNull(login);

		Sshj ssh = new Sshj(config, access.getPrompt());

		try (Mutex ignored = mutexes.getMutex(login.getHost())) {
			URI uri = URI.create("ssh://" + login.getUser() + "@" + login.getHostAndPort());
			Shell shell = new SshjShell(ssh, uri);
			ssh.connect(login);

			shells.add(shell);
			return new DecoratedShell(shell) {
				@Override
				public void close() throws IOException {
					shells.remove(shell);
					super.close();
				}
			};
		}
	}

	@Override
	public void close() {
		shells.forEach(Swallower::close);
	}
}
