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

import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.shell.Tunneler;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Sockets;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class AutoTunneler implements Tunneler {
	private static final Logger log = Logs.getLogger();

	private final Tunneler noTunneler = new NoTunneler();
	private final Tunneler delegate;
	private final long timeout;

	public AutoTunneler(@Nonnull Tunneler delegate, long timeout) {
		this.delegate = checkNotNull(delegate);
		this.timeout = timeout;
	}

	@Override
	@Nonnull
	public Tunnel getTunnel(@Nonnull String host, int port, @Nullable String through) throws IOException {
		checkNotNull(host);

		boolean allowed = through == null || host.equals(through);
		if (allowed && Sockets.check(host, port, timeout)) {
			log.debug("using direct connect to {}:{}", host, port);
			return noTunneler.getTunnel(host, port, through);
		} else {
			log.debug("using tunnel connect to {}:{}", host, port);
			return delegate.getTunnel(host, port, through);
		}
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}
}
