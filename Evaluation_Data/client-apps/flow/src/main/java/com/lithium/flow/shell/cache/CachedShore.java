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

import com.lithium.flow.access.Access;
import com.lithium.flow.access.Login;
import com.lithium.flow.config.Config;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.shell.Shore;
import com.lithium.flow.util.Recycler;
import com.lithium.flow.util.Reusable;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class CachedShore implements Shore {
	private final Shore delegate;
	private final Config config;
	private final Access access;
	private final Recycler<Login, Shell> shells;

	public CachedShore(@Nonnull Shore delegate, @Nonnull Config config, @Nonnull Access access) {
		this.delegate = checkNotNull(delegate);
		this.config = checkNotNull(config);
		this.access = checkNotNull(access);
		shells = new Recycler<>(config, login -> new PooledShell(config, delegate, login));
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
		Reusable<Shell> reusableShell = shells.get(login);
		return new DisposableShell(config, reusableShell);
	}

	@Override
	public void close() throws IOException {
		shells.close();
		delegate.close();
	}
}
