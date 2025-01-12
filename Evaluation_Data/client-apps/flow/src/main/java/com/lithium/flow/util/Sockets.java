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

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class Sockets {
	public static boolean check(@Nonnull String host, int port) {
		return check(host, port, 5000);
	}

	public static boolean check(@Nonnull String host, int port, long timeout) {
		checkNotNull(host);

		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port), timeout > Integer.MAX_VALUE ? 0 : (int) timeout);
			return socket.isConnected();
		} catch (Exception e) {
			return false;
		}
	}

	public static void waitForConnect(@Nonnull String host, int port, long timeout) throws IOException {
		long timeoutTime = System.currentTimeMillis() + timeout;
		InetSocketAddress address = new InetSocketAddress(host, port);

		while (true) {
			try (Socket socket = new Socket()) {
				socket.connect(address, (int) timeout);
				if (socket.isConnected()) {
					return;
				}
			} catch (IOException e) {
				if (System.currentTimeMillis() > timeoutTime) {
					throw e;
				}
				Sleep.softly(1000);
			}
		}
	}

	@Nonnull
	public static ServerSocket nextServerSocket(@Nonnull Config config) throws IOException {
		int scan = config.getInt("port.scan", 1);
		int minPort = config.getInt("port");
		int maxPort = minPort + scan;

		int port = minPort;
		while (port < maxPort) {
			try {
				return new ServerSocket(port);
			} catch (IOException e) {
				port++;
			}
		}

		if (scan == 1) {
			throw new IOException("port " + minPort + " not available");
		} else {
			throw new IOException("no ports available between " + minPort + " and " + maxPort);
		}
	}

	public static int nextServerPort(@Nonnull Config config) throws IOException {
		try (ServerSocket server = nextServerSocket(config)) {
			return server.getLocalPort();
		}
	}

	public static int nextFreePort() throws IOException {
		try (ServerSocket server = new ServerSocket(0)) {
			return server.getLocalPort();
		}
	}

	@Nonnull
	public static String getLocalAddress() throws IOException {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			for (NetworkInterface network : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if (network.isUp() && !network.isLoopback()) {
					for (InetAddress addr : Collections.list(network.getInetAddresses())) {
						if (!addr.isAnyLocalAddress() && !addr.isLoopbackAddress() && !addr.isMulticastAddress()) {
							return addr.getHostAddress();
						}
					}
				}
			}
		}

		throw new IOException("unknown local address");
	}
}
