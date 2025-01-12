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

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;
import com.lithium.flow.util.Measure;
import com.lithium.flow.util.Needle;
import com.lithium.flow.util.Progress;
import com.lithium.flow.util.Threader;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class ParallelRepo extends DecoratedRepo {
	private final Threader threader;
	private final long logTime;
	private final long avgTime;

	public ParallelRepo(@Nonnull Repo delegate, @Nonnull Config config) {
		super(checkNotNull(delegate));
		checkNotNull(config);

		threader = Threader.build(config);

		boolean progress = config.getBoolean("progress", false);
		logTime = progress ? config.getTime("progress.logTime", "5s") : 0;
		avgTime = progress ? config.getTime("progress.avgTime", "15s") : 0;
	}

	@Override
	@Nonnull
	public List<Config> getConfigs() throws IOException {
		Needle<Config> needle = threader.needle();
		Progress progress = new Progress();
		Measure names = progress.measure("names").useForEta();

		if (logTime > 0) {
			progress.start(logTime, avgTime);
		}

		try {
			getNames().forEach(name -> {
				names.incTodo();

				needle.submit(name, () -> {
					try {
						return getConfig(name);
					} finally {
						names.incDone();
					}
				});
			});

			return needle.toList();
		} finally {
			if (logTime > 0) {
				progress.close();
			}
		}
	}

	@Override
	@Nonnull
	public Stream<Config> streamConfigs() throws IOException {
		return getConfigs().stream();
	}
}
