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

package com.lithium.flow.key;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;
import static java.util.stream.Collectors.toList;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import javax.annotation.Nonnull;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Matt Ayres
 */
public class RepoKeySource implements KeySource {
	private final Repo provider;
	private final List<String> configKeys;

	public RepoKeySource(@Nonnull Repo repo, @Nonnull List<String> configKeys) {
		this.provider = checkNotNull(repo);
		this.configKeys = checkNotNull(configKeys);
	}

	@Override
	@Nonnull
	public List<Key> getKeys(@Nonnull String name) {
		try {
			Config config = provider.getConfig(name);
			return configKeys.stream().map(config::getString).map(bytes ->
					new SecretKeySpec(base16().decode(bytes), "AES")).collect(toList());
		} catch (IOException e) {
			throw new RuntimeException("failed to get keys: " + name, e);
		}
	}
}
