/*
 * Copyright 2019 Lithium Technologies, Inc.
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

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.RecordPath;
import com.lithium.flow.io.Swallower;
import com.lithium.flow.shell.Exec;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Needle;
import com.lithium.flow.util.Sleep;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.common.base.Joiner;

/**
 * @author Matt Ayres
 */
public class RunnerHost implements Closeable {
	private static final Logger log = Logs.getLogger();

	private final RunnerContext context;
	private final Config deployConfig;
	private final Config runnerConfig;
	private final VaultRun vaultRun;
	private final Needle<?> runNeedle;
	private final String host;
	private Exec runExec;

	public RunnerHost(@Nonnull Config config, @Nonnull Config deployConfig, @Nonnull RunnerContext context) {
		checkNotNull(config);
		this.deployConfig = checkNotNull(deployConfig);
		this.context = checkNotNull(context);

		runnerConfig = config.toBuilder().addAll(deployConfig.subset("runner")).build();
		vaultRun = new VaultRun(context.getVault(), context.getAccess().getPrompt(), runnerConfig);
		runNeedle = context.getRunThreader().needle();
		host = runnerConfig.getString("host");
	}

	@Nonnull
	public RunnerHost start() throws IOException {
		String name = runnerConfig.getString("name");
		String user = context.getAccess().getLogin(host).getUser();
		log.info("deploying {} to {}@{}", name, user, host);

		Filer destFiler = new FasterShellFiler(getShell().getFiler(), this::getShell);

		if (runnerConfig.getBoolean("kill.only", false)) {
			kill();
			return this;
		}

		String libDir = runnerConfig.getString("lib.dir");
		String moduleDir = runnerConfig.getString("module.dir");

		try (RunnerSync sync = new RunnerSync(runnerConfig, context, destFiler)) {
			sync.installJava();
			sync.syncLibs(libDir);
			sync.syncModules(moduleDir);
			sync.syncPaths();
		}

		kill();

		vaultRun.deploy(destFiler);

		String configOut = runnerConfig.getString("config.out");
		destFiler.createDirs(RecordPath.getFolder(configOut));
		writeConfig(deployConfig, destFiler.writeFile(configOut));
		log.debug("wrote: {}", configOut);

		List<String> classpathList = new ArrayList<>();
		context.getLibs().forEach(lib -> classpathList.add(libDir + "/" + RecordPath.getName(lib)));
		context.getModules().forEach(module -> classpathList.add(moduleDir + "/" + module));

		String classpath = Joiner.on(":").join(classpathList);
		log.debug("classpath: {}", classpath);

		for (String dir : runnerConfig.getList("dirs", Configs.emptyList())) {
			destFiler.createDirs(dir);
		}

		destFiler.close();

		context.getHostsMeasure().incDone();

		if (!runnerConfig.getBoolean("run", true)) {
			return this;
		}

		String prefix = runnerConfig.getString("prefix", runnerConfig.getString("name"));
		int pad = runnerConfig.getInt("prefixPad", 15);
		String paddedPrefix = StringUtils.rightPad(prefix, pad, '.') + " ";
		run(paddedPrefix, classpath, vaultRun.getEnv());

		return this;
	}

	private void run(@Nonnull String prefix, @Nonnull String classpath, @Nullable String env) {
		try {
			String destDir = runnerConfig.getString("dest.dir");
			String classpathPath = destDir + "/classpath";
			Shell shell = getShell();
			Filer filer = shell.getFiler();

			try (PrintStream ps = new PrintStream(filer.writeFile(classpathPath))) {
				ps.println(classpath);
			}

			List<String> commands = new ArrayList<>();
			commands.add("export CLASSPATH=$(cat " + classpathPath + ")");
			if (env != null) {
				commands.add(env);
			}
			commands.add("cd " + destDir);

			String command = runnerConfig.getString("java.command");
			if (runnerConfig.getTime("relay", "0") != 0) {
				commands.add("export RELAY_COMMAND='" + command + "'");
				commands.add(runnerConfig.getString("relay.command"));
			} else {
				commands.add(command);
			}

			if (runnerConfig.getBoolean("run.scripts", false)) {
				writeScripts(commands);
			}

			String readyString = runnerConfig.getString("run.readyString", null);
			AtomicBoolean ready = new AtomicBoolean();

			int logSkip = runnerConfig.getInt("log.skip", 0);
			AtomicInteger logCount = new AtomicInteger();

			commands.forEach(run -> log.debug("running: {}", run));
			runExec = shell.exec(commands);
			runNeedle.execute("out@" + host, () -> runExec.out().forEach(line -> {
				if (logCount.incrementAndGet() > logSkip) {
					System.out.println(prefix + line);
				}
				if (readyString != null && line.contains(readyString)) {
					ready.set(true);
				}
			}));
			runNeedle.execute("err@" + host, () -> runExec.err().forEach(line -> System.err.println(prefix + line)));

			if (readyString != null) {
				Sleep.until(ready::get);
				Sleep.softly(runnerConfig.getTime("run.readySleep", "0"));
			}
		} catch (IOException e) {
			log.warn("exec failed", e);
		}
	}

	private void writeScripts(@Nonnull List<String> commands) throws IOException {
		String name = runnerConfig.getString("name");
		String destDir = runnerConfig.getString("dest.dir");
		String startPath = destDir + "/start.sh";
		String stopPath = destDir + "/stop.sh";

		try (Shell shell = getShell()) {
			Filer filer = shell.getFiler();

			String logOut = runnerConfig.getString("log.out");
			List<String> startCommands = new ArrayList<>(commands);
			String lastCommand = startCommands.remove(startCommands.size() - 1);

			try (PrintStream ps = new PrintStream(filer.writeFile(startPath))) {
				ps.println("#!/bin/bash");
				startCommands.forEach(ps::println);
				ps.println("echo >> " + logOut);
				ps.println(lastCommand + " 1>/dev/null 2>&1 &");
				ps.println("tail -f " + logOut);
			}

			try (PrintStream ps = new PrintStream(filer.writeFile(stopPath))) {
				ps.println("#!/bin/bash");
				ps.println("echo >> " + logOut);
				if (runnerConfig.getTime("relay", "0") != 0) {
					ps.println("kill $(ps auxw | grep 'Drelay=" + name + " ' | grep -v grep | awk '{ print $2 }')");
				}
				ps.println("kill $(ps auxw | grep 'Dname=" + name + " ' | grep -v grep | awk '{ print $2 }')");
				ps.println("tail -f " + logOut);
			}

			shell.exec("chmod +x " + startPath).exit();
			shell.exec("chmod +x " + stopPath).exit();
		}
	}

	@Override
	public void close() {
		runNeedle.close();
		Swallower.close(runExec);
		log.debug("finished");
	}

	private void writeConfig(@Nonnull Config config, @Nonnull OutputStream out) throws IOException {
		boolean sorted = runnerConfig.getBoolean("config.sorted", true);
		Map<String, String> map = sorted ? config.asSortedMap() : config.asMap();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, UTF_8))) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				writer.write(entry.getKey() + " = " + entry.getValue() + "\r\n");
			}
		}
	}

	@Nullable
	private Integer getPid(@Nonnull String key) throws IOException {
		String name = runnerConfig.getString("name");
		String command = runnerConfig.getString("pid.command",
				"ps auxw | grep 'D{key}={name} ' | grep -v grep | awk '{ print $2 }'")
				.replace("{key}", key)
				.replace("{name}", name);

		log.debug("running: {}", command);
		String pid = getShell().exec(command).line();
		return pid.isEmpty() ? null : Integer.parseInt(pid);
	}

	private void killPid(@Nonnull Integer pid, boolean force) throws IOException {
		String command = runnerConfig.getString("kill.command", "kill -{signal} {pid}")
				.replace("{signal}", force ? "KILL" : "TERM")
				.replace("{pid}", String.valueOf(pid));
		getShell().exec(command).exit();
	}

	private void kill() throws IOException {
		kill("relay");
		kill("name");
	}

	private void kill(@Nonnull String key) throws IOException {
		String name = runnerConfig.getString("name");
		Integer pid = getPid(key);
		if (pid == null) {
			return;
		}

		if (!runnerConfig.getBoolean("kill", false)) {
			throw new IOException(Logs.message("pid {} already exists for {}={}", pid, key, name));
		}

		log.info("killing existing pid {} for {}={}", pid, key, name);
		killPid(pid, false);

		long maxTime = runnerConfig.getTime("kill.maxTime", "10s");
		long endTime = System.currentTimeMillis() + maxTime;

		while (System.currentTimeMillis() < endTime) {
			Sleep.softly(500);
			if (getPid(key) == null) {
				log.info("killed existing pid {} for {}={}", pid, key, name);
				return;
			}
		}

		if (!runnerConfig.getBoolean("kill.force", false)) {
			throw new IOException(Logs.message("failed to kill existing pid {} for {}={}", pid, key, name));
		}

		log.info("force killing existing pid {} for {}={}", pid, key, name);
		killPid(pid, true);

		endTime = System.currentTimeMillis() + maxTime;
		while (System.currentTimeMillis() < endTime) {
			Sleep.softly(500);
			if (getPid(key) == null) {
				log.info("force killed existing pid {} for {}={}", pid, key, name);
				return;
			}
		}

		throw new IOException(Logs.message("failed to force kill existing pid {} for {}={}", pid, key, name));
	}

	@Nonnull
	private Shell getShell() throws IOException {
		return context.getShore().getShell(host);
	}
}
