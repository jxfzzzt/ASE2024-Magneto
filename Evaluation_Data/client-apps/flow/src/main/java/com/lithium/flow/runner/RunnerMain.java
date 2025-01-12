/*
 * Copyright 2017 Lithium Technologies, Inc.
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

package com.lithium.flow.runner;

import static java.util.Collections.singletonList;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.ConfigLoader;
import com.lithium.flow.config.Configs;
import com.lithium.flow.config.loaders.FileConfigLoader;
import com.lithium.flow.util.HostUtils;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Main;
import com.lithium.flow.util.Sleep;
import com.lithium.flow.util.Threader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;

import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
public class RunnerMain {
	public RunnerMain(@Nonnull Config config) throws IOException {
		RunnerContext context = new RunnerContext(config);

		List<RunnerHost> runners = new CopyOnWriteArrayList<>();

		for (String path : config.getList("config")) {
			Threader parallel = new Threader(config.getInt("parallel", -1));

			File file = new File(path);
			ConfigLoader loader = new FileConfigLoader(file.getParent());
			Config deployConfig = Configs.newBuilder().addLoader(loader).addAll(config.subset("deploy"))
					.include(file.getAbsolutePath()).build();

			if (deployConfig.containsKey("runner.hosts")) {
				List<String> hosts = deployConfig.getList("runner.hosts", Splitter.on(' '));
				HostUtils.expand(hosts).forEach(runHost -> {
					Config runConfig = deployConfig.toBuilder().setString("runner.host", runHost).build();
					parallel.execute(runHost, () -> runners.add(new RunnerHost(config, runConfig, context).start()));
				});
			} else {
				List<String> nums = deployConfig.getList("runner.nums", singletonList("0"), Splitter.on(' '));
				HostUtils.expand(nums).forEach(num -> {
					Config runConfig = deployConfig.toBuilder().setString("num", num).build();
					parallel.execute(num, () -> runners.add(new RunnerHost(config, runConfig, context).start()));
				});
			}

			Sleep.softly(config.getTime("sleep", "0"));
			parallel.close();
		}

		context.getProgress().close();

		runners.forEach(RunnerHost::close);
		context.close();
	}

	public static void main(String[] args) throws IOException {
		Config config = Main.config();
		Logs.configure(config);
		new RunnerMain(config);
	}
}
