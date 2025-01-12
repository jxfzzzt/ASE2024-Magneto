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

package com.lithium.flow.vault;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Access;
import com.lithium.flow.access.Login;
import com.lithium.flow.access.Prompt;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class VaultAccess implements Access {
	private final Access delegate;
	private final Vault vault;

	public VaultAccess(@Nonnull Access delegate, @Nonnull Vault vault) {
		this.delegate = checkNotNull(delegate);
		this.vault = checkNotNull(vault);
	}

	@Override
	@Nonnull
	public Prompt getPrompt() {
		return delegate.getPrompt();
	}

	@Override
	@Nonnull
	public Login getLogin(@Nonnull String spec) throws IOException {
		checkNotNull(spec);

		String host = delegate.getLogin(spec).getHost();
		int port = Login.from(spec).getPort();

		for (String key : vault.getKeys()) {
			Login login = delegate.getLogin(key);
			if (host.matches(login.getHost())) {
				return login.toBuilder().setHost(host).build();
			}
		}

		return delegate.getLogin(spec).toBuilder().setHost(host).setPort(port).build();
	}
}
