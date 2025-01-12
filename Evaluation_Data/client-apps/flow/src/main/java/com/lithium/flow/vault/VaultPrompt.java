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

import com.lithium.flow.access.Prompt;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class VaultPrompt implements Prompt {
	private final Prompt delegate;
	private final Vault vault;

	public VaultPrompt(@Nonnull Prompt delegate, @Nonnull Vault vault) {
		this.delegate = checkNotNull(delegate);
		this.vault = checkNotNull(vault);
	}

	@Override
	@Nonnull
	public Response prompt(@Nonnull String name, @Nonnull String message, @Nonnull Type type) {
		checkNotNull(name);
		checkNotNull(message);

		switch (vault.getState()) {
			case NEW:
				while (true) {
					String password1 = delegate.prompt("master1", "Choose your master password: ", Type.MASKED).value();
					String password2 = delegate.prompt("master2", "Retype your master password: ", Type.MASKED).value();
					if (password1.equals(password2)) {
						vault.setup(password1);
						break;
					}
				}
				break;

			case LOCKED:
				String password;
				do {
					password = delegate.prompt("master", "Enter your master password: ", Type.MASKED).value();
				} while (!vault.unlock(password));
				break;

			case UNLOCKED:
				break;
		}

		String secret = getSecret(name, message, type);
		return Response.build(secret, valid -> vault.putValue(name, valid ? secret : null));
	}

	@Nonnull
	private String getSecret(@Nonnull String name, @Nonnull String message, @Nonnull Type type) {
		String secret = vault.getValue(name);
		if (secret == null) {
			for (String regex : vault.getKeys()) {
				if (name.matches(regex)) {
					secret = vault.getValue(regex);
					break;
				}
			}
		}

		if (secret == null) {
			secret = delegate.prompt(name, message, type).value();
			vault.putValue(name, secret);
		}

		return secret;
	}
}
