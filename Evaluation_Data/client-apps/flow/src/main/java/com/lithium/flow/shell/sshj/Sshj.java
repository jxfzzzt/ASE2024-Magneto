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

package com.lithium.flow.shell.sshj;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Login;
import com.lithium.flow.access.Prompt;
import com.lithium.flow.access.Prompt.Response;
import com.lithium.flow.access.Prompt.Type;
import com.lithium.flow.config.Config;
import com.lithium.flow.io.Swallower;
import com.lithium.flow.util.Unchecked;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SecurityUtils;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.userauth.password.PasswordFinder;
import net.schmizz.sshj.userauth.password.Resource;

/**
 * @author Matt Ayres
 */
public class Sshj extends SSHClient {
	private final Prompt prompt;
	private final boolean pty;
	private final int retries;
	private final int filerBuffer;
	private final long initTimeout;

	public Sshj(@Nonnull Config config, @Nonnull Prompt prompt) throws IOException {
		checkNotNull(config);
		this.prompt = checkNotNull(prompt);

		getConnection().setMaxPacketSize(config.getInt("shell.maxPacketSize", getConnection().getMaxPacketSize()));
		getConnection().setWindowSize(config.getLong("shell.windowSize", getConnection().getWindowSize()));

		long defaultTimeout = config.getTime("shell.timeout", "10s");
		initTimeout = config.getTime("shell.timeout.init", String.valueOf(defaultTimeout));
		getConnection().setTimeoutMs((int) config.getTime("shell.timeout.connection", String.valueOf(defaultTimeout)));
		getTransport().setTimeoutMs((int) config.getTime("shell.timeout.transport", String.valueOf(defaultTimeout)));

		if (config.getBoolean("shell.compress", true)) {
			useCompression();
		}

		if (config.getBoolean("shell.promiscuous", true)) {
			addHostKeyVerifier(new PromiscuousVerifier());
		} else {
			if (config.getBoolean("shell.loadKnownHosts", true)) {
				if (config.containsKey("shell.knownHosts")) {
					loadKnownHosts(new File(config.getString("shell.knownHosts")));
				} else {
					loadKnownHosts();
				}
			}

			addHostKeyVerifier((host, port, key) -> {
				String fingerprint = SecurityUtils.getFingerprint(key);
				String name = "fingerprint[" + fingerprint + "]@" + host;
				String message = "Enter yes/no to verify fingerprint " + fingerprint + " for " + host + ": ";
				return prompt.prompt(name, message, Type.PLAIN).accept().equals("yes");
			});
		}

		pty = config.getBoolean("shell.pty", false);
		retries = config.getInt("shell.retries", 3);
		filerBuffer = config.getInt("shell.filer.buffer", 64);
	}

	public void connect(@Nonnull Login login) throws IOException {
		CountDownLatch latch = new CountDownLatch(1);
		Thread thread = new Thread(() -> Unchecked.run(() -> {
			if (!latch.await(initTimeout, TimeUnit.MILLISECONDS)) {
				close();
			}
		}));
		thread.setName("Sshj.connect:" + login.getUser() + "@" + login.getHost());
		thread.setDaemon(true);
		thread.start();

		log.debug("connect: {}", login);
		connect(login.getHost(), login.getPortOrDefault(22));
		latch.countDown();

		UserAuthException exception = null;
		String keyPath = login.getKeyPath();

		for (int i = 0; i < retries + 1; i++) {
			if (keyPath != null) {
				if (new File(keyPath).isFile()) {
					Response pass = prompt(keyPath, "Enter passphrase for {name}: ", Type.MASKED);
					try {
						authPublickey(login.getUser(), loadKeys(keyPath, pass.value()));
						pass.accept();
						return;
					} catch (UserAuthException e) {
						exception = e;
						pass.reject();
					}
				} else {
					Response key = prompt("key[" + keyPath + "]", "Enter private key for {name}: ", Type.BLOCK);
					Response pass = prompt("pass[" + keyPath + "]", "Enter passphrase for {name}: ", Type.MASKED);

					try {
						authPublickey(login.getUser(), loadKeys(key.value(), null, new PasswordFinder() {
							@Override
							public char[] reqPassword(Resource<?> resource) {
								return pass.value().toCharArray();
							}

							@Override
							public boolean shouldRetry(Resource<?> resource) {
								return false;
							}
						}));
						key.accept();
						pass.accept();
						return;
					} catch (UserAuthException e) {
						exception = e;
					}
				}
			} else {
				Response pass = prompt(login.getDisplayString(), "Enter password for {name}: ", Type.MASKED);
				try {
					authPassword(login.getUser(), pass.value());
					pass.accept();
					return;
				} catch (UserAuthException e) {
					exception = e;
					pass.reject();
				}
			}
		}

		if (exception != null) {
			Swallower.close(this);
			throw exception;
		}
	}

	@Nonnull
	private Response prompt(@Nonnull String name, @Nonnull String message, @Nonnull Type type) {
		return prompt.prompt(name, message.replace("{name}", name), type);
	}

	public boolean isPty() {
		return pty;
	}

	public int getRetries() {
		return retries;
	}

	public int getFilerBuffer() {
		return filerBuffer;
	}
}
