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

package com.lithium.flow.shell;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Access;
import com.lithium.flow.access.Login;
import com.lithium.flow.access.Prompt;
import com.lithium.flow.config.Config;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class ShellAccess implements Access {
	private final Config config;
	private final Prompt prompt;

	public ShellAccess(@Nonnull Config config, @Nonnull Prompt prompt) {
		this.config = checkNotNull(config);
		this.prompt = checkNotNull(prompt);
	}

	@Override
	@Nonnull
	public Prompt getPrompt() {
		return prompt;
	}

	@Override
	@Nonnull
	public Login getLogin(@Nonnull String spec) {
		checkNotNull(spec);

		Login login = Login.from(spec);
		String host = login.getHost();
		String user = config.getMatch("shell.users", host).orElse(login.getUser());
		String keyPath = config.getMatch("shell.keys", host).orElse(null);
		return Login.newBuilder().setUser(user).setHost(host).setKeyPath(keyPath).build();
	}
}
