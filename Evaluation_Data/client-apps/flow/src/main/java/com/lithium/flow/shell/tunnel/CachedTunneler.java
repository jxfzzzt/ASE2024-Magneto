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

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.shell.Tunneler;
import com.lithium.flow.shell.cache.DisposableTunnel;
import com.lithium.flow.util.Recycler;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

/**
 * @author Matt Ayres
 */
public class CachedTunneler implements Tunneler {
	private final Tunneler delegate;
	private final Recycler<Triple<String, Integer, String>, Tunnel> tunnels;

	public CachedTunneler(@Nonnull Tunneler delegate) {
		this(Configs.empty(), delegate);
	}

	public CachedTunneler(@Nonnull Config config, @Nonnull Tunneler delegate) {
		this.delegate = checkNotNull(delegate);
		tunnels = new Recycler<>(config, t -> delegate.getTunnel(t.getLeft(), t.getMiddle(), t.getRight()));
	}

	@Override
	@Nonnull
	public Tunnel getTunnel(@Nonnull String host, int port, @Nullable String through) throws IOException {
		return new DisposableTunnel(tunnels.get(Triple.of(host, port, through)));
	}

	@Override
	public void close() throws IOException {
		tunnels.close();
		delegate.close();
	}
}
