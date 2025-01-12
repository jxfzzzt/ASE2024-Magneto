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

package com.lithium.flow.config.repos;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.config.Repo;
import com.lithium.flow.matcher.StringMatcher;
import com.lithium.flow.matcher.StringMatchers;
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class FilteredRepo extends DecoratedRepo {
	private static final Logger log = Logs.getLogger();

	private final Config config;

	public FilteredRepo(@Nonnull Repo delegate, @Nonnull Config config) {
		super(delegate);
		this.config = checkNotNull(config);
	}

	@Override
	@Nonnull
	public List<String> getNames() throws IOException {
		List<String> configNames;
		if (config.getString("configs", "*").equals("*")) {
			configNames = super.getNames();
		} else {
			configNames = config.getList("configs", Configs.emptyList());
		}

		configNames = new ArrayList<>(configNames);

		if (config.containsKey("configs.matcher")) {
			StringMatcher matcher = StringMatchers.fromConfig(config, "configs.matcher");
			configNames = configNames.stream().filter(matcher).collect(toList());
		}

		configNames.removeAll(config.getList("configs.skip", Configs.emptyList()));

		int limit = config.getInt("configs.limit", -1);
		if (limit > -1 && limit < configNames.size()) {
			configNames = configNames.subList(0, limit);
		}

		log.debug("found {} configs", configNames.size());
		return configNames;
	}
}
