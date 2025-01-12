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
import static java.util.stream.Collectors.toList;

import com.lithium.flow.access.Access;
import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;
import com.lithium.flow.ioc.Locator;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Keys {
	@Nonnull
	public static KeySource buildKeySource(@Nonnull Locator locator) {
		checkNotNull(locator);
		List<KeySource> sources = locator.getInstance(Config.class).getList("key.source").stream()
				.map(provider -> buildKeySource(locator, provider)).collect(toList());
		return new CachedKeySource(new CompositeKeySource(sources));
	}

	@Nonnull
	private static KeySource buildKeySource(@Nonnull Locator locator, @Nonnull String provider) {
		Config config = locator.getInstance(Config.class);
		switch (provider) {
			case "config":
				Repo repo = locator.getInstance(Repo.class);
				List<String> configKeys = config.getList("key.configs");
				return new RepoKeySource(repo, configKeys);
			case "prompt":
				Access access = locator.getInstance(Access.class);
				String group = config.getString("key.prompt");
				return new PromptKeySource(access.getPrompt(), group);
			case "redis":
				return new RedisKeySource(config);
			default:
				throw new IllegalArgumentException("unknown provider: " + provider);
		}
	}
}
