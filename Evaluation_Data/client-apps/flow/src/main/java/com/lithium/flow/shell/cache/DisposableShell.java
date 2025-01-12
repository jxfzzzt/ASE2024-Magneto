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

package com.lithium.flow.shell.cache;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.DecoratedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.shell.Exec;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.shell.Tunneling;
import com.lithium.flow.util.Recycler;
import com.lithium.flow.util.Reusable;

import java.io.IOException;
import java.net.URI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public class DisposableShell implements Shell {
	private final Reusable<Shell> reusable;
	private final Recycler<Tunneling, Tunnel> tunnels;
	private final Recycler<Shell, Filer> filers;

	public DisposableShell(@Nonnull Config config, @Nonnull Reusable<Shell> reusable) {
		checkNotNull(config);
		this.reusable = checkNotNull(reusable);
		tunnels = new Recycler<>(config, tunneling -> reusable.get(this).tunnel(tunneling));
		filers = new Recycler<>(config, shell -> reusable.get(this).getFiler());
	}

	@Override
	@Nonnull
	public URI getUri() {
		return reusable.get(this).getUri();
	}

	@Override
	@Nonnull
	public Exec exec(@Nonnull String command, @Nullable Boolean pty) throws IOException {
		checkNotNull(command);
		return reusable.get(this).exec(command, pty);
	}

	@Override
	@Nonnull
	public Tunnel tunnel(@Nonnull Tunneling tunneling) throws IOException {
		checkNotNull(tunneling);
		Reusable<Tunnel> reusableTunnel = tunnels.get(tunneling);
		return new DisposableTunnel(reusableTunnel);
	}

	@Override
	@Nonnull
	public Filer getFiler() throws IOException {
		Reusable<Filer> reusableFiler = filers.get(this);
		return new DecoratedFiler(reusableFiler.get(this)) {
			@Override
			public void close() {
				reusableFiler.recycle(this);
			}
		};
	}

	@Override
	public void close() {
		tunnels.close();
		filers.close();
		reusable.recycle(this);
	}
}
