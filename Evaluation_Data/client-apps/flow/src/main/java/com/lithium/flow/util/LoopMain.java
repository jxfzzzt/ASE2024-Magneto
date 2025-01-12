/*
 * Copyright 2018 Lithium Technologies, Inc.
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

package com.lithium.flow.util;

import com.lithium.flow.config.Config;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class LoopMain {
	public LoopMain(@Nonnull Config config) throws ClassNotFoundException, InterruptedException {
		Class<?> clazz = Class.forName(config.getString("run.class"));
		Executable executable = () -> Main.run(clazz, config);
		LoopThread thread = LoopThread.from(config.prefix("run"), executable);
		thread.join();
	}

	public static void main(String[] args) {
		Main.run();
	}
}
