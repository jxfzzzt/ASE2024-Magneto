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

import com.lithium.flow.config.Config;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.LoopThread;
import com.lithium.flow.util.Main;
import com.lithium.flow.util.Sleep;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class AgentServer {
	private static final Logger log = Logs.getLogger();

	public AgentServer(@Nonnull Config config) throws IOException {
		checkNotNull(config);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(System.in, baos);
		byte[] bytes = baos.toByteArray();

		ServerSocket server = new ServerSocket(config.getInt("agent.port"), -1, InetAddress.getByName(null));
		long endTime = System.currentTimeMillis() + config.getTime("agent.maximumTime", "1d");
		long inactiveTime = config.getTime("agent.inactiveTime", "8h");
		AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());

		new LoopThread(() -> {
			try {
				Socket socket = server.accept();
				log.info("accepted connection: {}", socket);
				try (OutputStream out = socket.getOutputStream()) {
					IOUtils.copy(new ByteArrayInputStream(bytes), out);
				}
			} catch (IOException e) {
				//
			}

			lastTime.set(System.currentTimeMillis());
		});

		new LoopThread(1000, () -> {
			long time = System.currentTimeMillis();
			if (time > endTime) {
				log.info("maximum time reached");
				System.exit(0);
			}

			if (time > lastTime.get() + inactiveTime) {
				log.info("inactive time reached");
				System.exit(0);
			}
		});

		log.info("started agent on port {}", server.getLocalPort());
		Sleep.forever();
	}

	public static void main(String[] args) {
		Main.run();
	}
}
