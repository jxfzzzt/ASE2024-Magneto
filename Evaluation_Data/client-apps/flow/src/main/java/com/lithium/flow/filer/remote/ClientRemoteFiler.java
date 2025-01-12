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

package com.lithium.flow.filer.remote;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.DecoratedFiler;

import java.io.IOException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class ClientRemoteFiler extends DecoratedFiler implements RemoteFiler {
	public ClientRemoteFiler(@Nonnull Config config) throws IOException {
		this(URI.create(config.getString("url")).getHost(), URI.create(config.getString("url")).getPort());
	}

	public ClientRemoteFiler(@Nonnull String host, int port) throws IOException {
		super(lookup(checkNotNull(host), port));
		bypassDelegateFind = true;
	}

	@Nonnull
	private static RemoteFiler lookup(@Nonnull String host, int port) throws IOException {
		checkNotNull(host);
		String name = "//" + host + ":" + port + "/" + BIND;
		try {
			return (RemoteFiler) Naming.lookup(name);
		} catch (NotBoundException e) {
			throw new IOException("failed lookup: " + name, e);
		}
	}
}
