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
import static java.util.stream.Collectors.toMap;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.RecordPath;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.util.Lazy;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Measure;
import com.lithium.flow.util.Needle;
import com.lithium.flow.util.Once;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class RunnerSync implements Closeable {
	private static final Logger log = Logs.getLogger();

	private final Config config;
	private final RunnerContext context;
	private final Filer destFiler;
	private final Shell shell;
	private final Needle<?> needle;

	public RunnerSync(@Nonnull Config config, @Nonnull RunnerContext context, @Nonnull Filer destFiler)
			throws IOException {
		this.config = checkNotNull(config);
		this.context = checkNotNull(context);
		this.destFiler = checkNotNull(destFiler);

		shell = context.getShore().getShell(config.getString("host"));
		needle = context.getSyncNeedle();
	}

	public void installJava() {
		String check = config.getString("java.check");
		String md5 = config.getString("java.md5");
		String install = config.getString("java.install");
		String link = config.getString("java.link", "");

		needle.execute("java", () -> {
			String checkMd5 = shell.exec(check).line();
			log.debug("java check md5: {}", checkMd5);
			if (!md5.equals(checkMd5)) {
				log.debug("java install: " + install);
				shell.exec(install).exit();
			}

			if (!link.isEmpty()) {
				log.debug("java link: " + link);
				shell.exec(link).exit();
			}
		});
	}

	public void syncLibs(@Nonnull String dir) throws IOException {
		Measure measure = context.getLibsMeasure();
		measure.addTodo(context.getLibs().size());

		Map<String, Long> sizes = getSizes(dir);
		if (sizes.isEmpty()) {
			destFiler.createDirs(dir);
		}

		for (String lib : context.getLibs()) {
			needle.execute("lib:" + lib, () -> {
				Record record = context.getFiler().getRecord(lib);
				Long size = sizes.get(record.getName());
				if (size == null || size != record.getSize()) {
					if (!context.getJarProvider().copy(record.getPath(), shell, destFiler, dir)) {
						sync(record.getPath(), dir + "/" + record.getName());
					}
				}
				measure.incDone();
			});
		}
	}

	public void syncModules(@Nonnull String dir) throws IOException {
		Measure measure = context.getModulesMeasure();
		measure.addTodo(context.getModules().size());

		Map<String, Long> sizes = getSizes(dir);
		if (sizes.isEmpty()) {
			destFiler.createDirs(dir);
		}

		for (String module : context.getModules()) {
			needle.execute("module:" + module, () -> {
				Long size = sizes.get(module);
				Lazy<byte[]> lazy = context.getBytes(module);
				if (size == null || size != lazy.get().length) {
					try (OutputStream out = destFiler.writeFile(dir + "/" + module)) {
						IOUtils.copy(new ByteArrayInputStream(lazy.get()), out);
					}
				}
				measure.incDone();
			});
		}
	}

	public void syncPaths() {
		List<String> paths = config.getList("paths", Collections.emptyList());
		String destDir = config.getString("dest.dir");
		Measure measure = context.getFilesMeasure();

		needle.execute("paths", () -> {
			Once<String> once = new Once<>(destFiler::createDirs);

			for (String path : paths) {
				RecordPath recordPath = RecordPath.from(new File(path).getCanonicalPath());
				String prefix = recordPath.getFolder() + "/";

				context.getFiler().findRecords(path, 1).filter(Record::isFile).forEach(record -> {
					measure.incTodo();
					needle.execute("path:" + record.getPath(), () -> {
						String srcPath = new File(record.getPath()).getCanonicalPath();
						String destPath = destDir + "/" + record.getPath().replace(prefix, "");
						once.accept(RecordPath.getFolder(destPath));
						sync(srcPath, destPath);
						measure.incDone();
					});
				});
			}
		});
	}

	@Nonnull
	private Map<String, Long> getSizes(@Nonnull String dir) throws IOException {
		return destFiler.listRecords(dir).stream().collect(toMap(Record::getName, Record::getSize));
	}

	private void sync(@Nonnull String srcPath, @Nonnull String destPath) throws IOException {
		try (InputStream in = context.getFiler().readFile(srcPath)) {
			try (OutputStream out = destFiler.writeFile(destPath)) {
				IOUtils.copy(in, out);
			}
		}
	}

	@Override
	public void close() {
		needle.close();
	}
}
