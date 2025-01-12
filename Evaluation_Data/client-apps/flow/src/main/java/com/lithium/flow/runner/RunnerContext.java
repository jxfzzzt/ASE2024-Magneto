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

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

import com.lithium.flow.access.Access;
import com.lithium.flow.config.Config;
import com.lithium.flow.filer.CachedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.LocalFiler;
import com.lithium.flow.filer.Record;
import com.lithium.flow.shell.Shells;
import com.lithium.flow.shell.Shore;
import com.lithium.flow.util.Lazy;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Measure;
import com.lithium.flow.util.Needle;
import com.lithium.flow.util.Progress;
import com.lithium.flow.util.Threader;
import com.lithium.flow.vault.Vault;
import com.lithium.flow.vault.Vaults;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import com.google.common.base.Splitter;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * @author Matt Ayres
 */
public class RunnerContext {
	private static final Logger log = Logs.getLogger();

	private final Config config;
	private final Filer filer;
	private final Vault vault;
	private final Access access;
	private final Shore shore;
	private final JarProvider jarProvider;
	private final Threader syncThreader;
	private final Threader runThreader;

	private final Progress progress;
	private final Measure hostsMeasure;
	private final Measure libsMeasure;
	private final Measure modulesMeasure;
	private final Measure filesMeasure;

	private final List<String> libs = new ArrayList<>();
	private final List<String> modules = new ArrayList<>();
	private final Map<String, Lazy<byte[]>> bytes = new HashMap<>();

	public RunnerContext(@Nonnull Config config) throws IOException {
		this.config = checkNotNull(config);

		filer = new CachedFiler(new LocalFiler(), config);
		vault = Vaults.buildVault(config);
		access = Vaults.buildAccess(config, vault);
		shore = Shells.buildShore(config, access);
		syncThreader = new Threader(config.getInt("sync.threads", 50))
				.withNeedlePermits(config.getInt("sync.needlePermits", 8));
		runThreader = Threader.forDaemon(config.getInt("run.threads", -1));
		jarProvider = JarProvider.build(config, access, filer);

		progress = Progress.start(config);
		hostsMeasure = progress.counter("hosts");
		libsMeasure = progress.counter("libs").useForEta();
		modulesMeasure = progress.counter("modules").useForEta();
		filesMeasure = progress.counter("files").useForEta();

		scanClassPath();
	}

	private void scanClassPath() throws IOException {
		String javaHome = System.getProperty("java.home").replaceAll("/jre$", "");
		String javaClassPath = System.getProperty("java.class.path", "");
		List<String> classPaths = Splitter.on(File.pathSeparator)
				.splitToList(javaClassPath).stream()
				.filter(p -> !p.startsWith(javaHome))
				.map(p -> p.replace(File.separatorChar, '/'))
				.collect(toList());

		for (String classPath : classPaths) {
			log.debug("classpath: {}", classPath);

			if (classPath.endsWith(".jar")) {
				libs.add(classPath);
			} else {
				Hasher hasher = Hashing.murmur3_128().newHasher();
				List<Record> records = filer.findRecords(classPath, 1)
						.filter(Record::isFile)
						.sorted(Record.pathAsc())
						.collect(toList());

				if (records.isEmpty()) {
					continue;
				}

				records.forEach(r -> {
					hasher.putUnencodedChars(r.getPath());
					hasher.putLong(r.getSize());
					hasher.putLong(r.getTime());
				});

				String name = "runner_" + hasher.hash().toString() + ".jar";
				modules.add(name);
				bytes.put(name, new Lazy<>(() -> buildJar(classPath, records)));
			}
		}
	}

	@Nonnull
	private byte[] buildJar(@Nonnull String dir, @Nonnull List<Record> records) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try (ZipOutputStream out = new ZipOutputStream(baos)) {
			for (Record record : records) {
				out.putNextEntry(new ZipEntry(record.getPath().replace(dir + "/", "")));
				try (InputStream in = filer.readFile(record.getPath())) {
					IOUtils.copy(in, out);
				}
				out.closeEntry();
			}
		}

		return baos.toByteArray();
	}

	public void close() throws IOException {
		syncThreader.close();
		runThreader.close();
		shore.close();
	}

	@Nonnull
	public Filer getFiler() {
		return filer;
	}

	@Nonnull
	public Config getConfig() {
		return config;
	}

	@Nonnull
	public Vault getVault() {
		return vault;
	}

	@Nonnull
	public Access getAccess() {
		return access;
	}

	@Nonnull
	public Shore getShore() {
		return shore;
	}

	@Nonnull
	public Needle<?> getSyncNeedle() {
		return syncThreader.needle();
	}

	@Nonnull
	public Threader getRunThreader() {
		return runThreader;
	}

	@Nonnull
	public JarProvider getJarProvider() {
		return jarProvider;
	}

	@Nonnull
	public List<String> getLibs() {
		return libs;
	}

	@Nonnull
	public List<String> getModules() {
		return modules;
	}

	@Nonnull
	public Lazy<byte[]> getBytes(@Nonnull String sync) {
		return bytes.get(sync);
	}

	@Nonnull
	public Progress getProgress() {
		return progress;
	}

	@Nonnull
	public Measure getHostsMeasure() {
		return hostsMeasure;
	}

	@Nonnull
	public Measure getLibsMeasure() {
		return libsMeasure;
	}

	@Nonnull
	public Measure getModulesMeasure() {
		return modulesMeasure;
	}

	@Nonnull
	public Measure getFilesMeasure() {
		return filesMeasure;
	}
}
