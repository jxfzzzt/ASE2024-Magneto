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

import static java.util.stream.Collectors.toList;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.store.FileStore;
import com.lithium.flow.store.MemoryStore;
import com.lithium.flow.util.CheckedSupplier;
import com.lithium.flow.util.Lines;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.LoopThread;
import com.lithium.flow.util.Main;
import com.lithium.flow.util.Needle;
import com.lithium.flow.util.Passwords;
import com.lithium.flow.util.Threader;
import com.lithium.flow.util.TimeUtils;
import com.lithium.flow.vault.SecureVault;
import com.lithium.flow.vault.Vault;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
public class RelayMain {
	private static final Logger log = Logs.getLogger();

	private final AtomicReference<Process> currentProcess = new AtomicReference<>();
	private final Threader threader = new Threader();
	private final CheckedSupplier<String, IOException> vaultPassword;
	private final List<String> command;
	private final boolean oomeRestart;
	private final long destroyMaxTime;
	private volatile boolean restart;

	public RelayMain(@Nonnull Config config) throws Exception {
		vaultPassword = buildVaultPassword(config);
		command = buildCommand();
		oomeRestart = config.getBoolean("runner.relay.oomeRestart", false);
		destroyMaxTime = config.getTime("runner.relay.destroyMaxTime", "30s");
		boolean keepAlive = config.getBoolean("runner.relay.keepAlive", false);

		String interval = config.getString("runner.relay", "0");
		if (TimeUtils.getMillisValue(interval) > 0) {
			Config loopConfig = config.toBuilder().setString("runner.relay.loopInterval", interval).build();
			LoopThread.from(loopConfig.prefix("runner.relay"), this::destroy);
		}

		do {
			start();
		} while (keepAlive || restart);

		threader.close();
	}

	private void start() throws IOException, InterruptedException {
		restart = false;

		log.info("starting process");
		ProcessBuilder pb = new ProcessBuilder(command);

		String password = vaultPassword.get();
		if (password != null) {
			Map<String, String> env = pb.environment();
			env.put("VAULT_PASSWORD", password);
		}

		Process process = pb.start();
		currentProcess.set(process);

		try (Needle<?> needle = threader.needle()) {
			needle.execute("out", () -> pipe(process.getInputStream(), System.out));
			needle.execute("err", () -> pipe(process.getErrorStream(), System.err));
		}

		process.waitFor();

		log.info("exited process: {}", process.exitValue());
	}

	private void pipe(@Nonnull InputStream in, @Nonnull PrintStream ps) throws IOException, InterruptedException {
		for (String line : Lines.iterate(in)) {
			ps.println(line);

			if (oomeRestart && line.contains("java.lang.OutOfMemoryError")) {
				log.info("detected OOME");
				destroy();
			}
		}
	}

	private void destroy() throws InterruptedException {
		Process process = currentProcess.getAndSet(null);
		if (process != null) {
			log.info("destroying process");
			process.destroy();

			if (!process.waitFor(destroyMaxTime, TimeUnit.MILLISECONDS)) {
				log.info("forcibly destroying process");
				process.destroyForcibly();
				process.waitFor();
			}

			restart = true;
		}
	}

	@Nonnull
	private List<String> buildCommand() {
		List<String> list = Splitter.on(' ').splitToList(System.getenv("RELAY_COMMAND"));
		return list.stream().filter(item -> !item.isEmpty()).collect(toList());
	}

	@Nonnull
	private CheckedSupplier<String, IOException> buildVaultPassword(@Nonnull Config config) {
		if (!config.containsKey("vault.path")) {
			return () -> null;
		}

		File file = new File(config.getString("vault.path"));
		Vault memoryVault = new SecureVault(Configs.empty(), new MemoryStore(new FileStore(file)));
		memoryVault.unlock(System.getenv("VAULT_PASSWORD"));

		return () -> {
			if (!file.delete()) {
				log.warn("failed to delete vault: {}", file.getAbsolutePath());
			}

			String password = Passwords.create(34);
			Vault fileVault = new SecureVault(Configs.empty(), new FileStore(file));
			fileVault.setup(password);
			memoryVault.getKeys().forEach(key -> fileVault.putValue(key, memoryVault.getValue(key)));
			return password;
		};
	}

	public static void main(String[] args) {
		Main.run();
	}
}
