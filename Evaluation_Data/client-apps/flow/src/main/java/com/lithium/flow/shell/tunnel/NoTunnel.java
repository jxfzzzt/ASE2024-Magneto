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

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class NoTunnel implements Tunnel {
	private final String host;
	private final int port;

	public NoTunnel(String host, int port) {
		this.host = checkNotNull(host);
		this.port = port;
	}

	@Override
	@Nonnull
	public String getHost() {
		return host;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void close() {
	}
}
