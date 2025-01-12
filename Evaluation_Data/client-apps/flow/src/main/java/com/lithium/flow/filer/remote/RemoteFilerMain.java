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

package com.lithium.flow.filer.remote;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Filers;
import com.lithium.flow.util.Main;

import java.io.IOException;

/**
 * @author Matt Ayres
 */
public class RemoteFilerMain {
	public RemoteFilerMain(Config config) throws IOException {
		setProperty("java.rmi.server.hostname", config, "rmi.hostname");
		setProperty("com.healthmarketscience.rmiio.exporter.port", config, "rmiio.port");
		Filer filer = Filers.buildFiler(config);
		new ServerRemoteFiler(filer, config.getInt("rmi.port", 3499), config.getInt("rmi.localport", 0));
	}

	private static void setProperty(String property, Config config, String key) {
		if (config.containsKey(key)) {
			System.setProperty(property, config.getString(key));
		}
	}

	public static void main(String[] args) {
		Main.run();
	}
}
