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

import com.lithium.flow.config.Configs;
import com.lithium.flow.store.MemoryStore;
import com.lithium.flow.store.Store;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Passwords;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Matt Ayres
 */
public class AgentVault implements Vault {
	private static final Logger log = Logs.getLogger();

	private final ObjectMapper mapper = new ObjectMapper();
	private final Vault delegate;
	private final Store store;

	public AgentVault(@Nonnull Vault delegate, @Nonnull Store store) {
		this.delegate = checkNotNull(delegate);
		this.store = checkNotNull(store);
	}

	@Override
	@Nonnull
	public State getState() {
		autoUnlock();
		return delegate.getState();
	}

	@Override
	public void setup(@Nonnull String password) {
		checkNotNull(password);
		delegate.setup(password);
		startAgent(password);
	}

	@Override
	public boolean unlock(@Nonnull String password) {
		checkNotNull(password);

		if (!delegate.unlock(password)) {
			return false;
		}

		if (!password.equals(readAgent())) {
			startAgent(password);
		}

		return true;
	}

	@Override
	public void lock() {
		delegate.lock();
	}

	@Override
	public void putValue(@Nonnull String key, @Nullable String secret) {
		checkNotNull(key);
		autoUnlock();
		delegate.putValue(key, secret);
	}

	@Override
	@Nullable
	public String getValue(@Nonnull String key) {
		checkNotNull(key);
		autoUnlock();
		return delegate.getValue(key);
	}

	@Override
	@Nonnull
	public Set<String> getKeys() {
		return delegate.getKeys();
	}

	private void autoUnlock() {
		if (delegate.getState() == State.LOCKED) {
			String password = readAgent();
			if (password != null) {
				delegate.unlock(password);
			}
		}
	}

	@Nullable
	private String readAgent() {
		String agentPort = store.getValue("vault.agent.port");
		String agentPassword = store.getValue("vault.agent.password");
		if (agentPort == null || agentPassword == null) {
			return null;
		}

		try {
			int port = Integer.parseInt(agentPort);
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("localhost", port), 5000);

			@SuppressWarnings("unchecked")
			Map<String, String> map = mapper.readValue(socket.getInputStream(), Map.class);

			Store agentStore = new MemoryStore(map);
			Vault agentVault = new SecureVault(Configs.empty(), agentStore);
			agentVault.unlock(agentPassword);
			return agentVault.getValue("password");
		} catch (Exception e) {
			log.debug("failed to read from agent", e);
			return null;
		}
	}

	private void startAgent(@Nonnull String password) {
		try {
			int port = findFreePort();
			String agentPassword = Passwords.create(32);

			Map<String, String> map = new HashMap<>();
			Store agentStore = new MemoryStore(map);
			Vault agentVault = new SecureVault(Configs.empty(), agentStore);
			agentVault.setup(agentPassword);
			agentVault.putValue("password", password);

			ProcessBuilder builder = new ProcessBuilder();
			builder.command(System.getProperty("java.home") + "/bin/java",
					"-Dagent.port=" + port, AgentServer.class.getName());
			builder.environment().put("CLASSPATH", System.getProperty("java.class.path"));
			Process process = builder.start();

			OutputStream out = process.getOutputStream();
			mapper.writeValue(out, map);
			out.close();

			store.putValue("vault.agent.port", String.valueOf(port));
			store.putValue("vault.agent.password", agentPassword);
		} catch (IOException e) {
			throw new VaultException("failed to start agent", e);
		}
	}

	private int findFreePort() throws IOException {
		try (ServerSocket server = new ServerSocket(0)) {
			return server.getLocalPort();
		}
	}
}
