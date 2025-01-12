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

import com.lithium.flow.access.Access;
import com.lithium.flow.config.Config;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.RecordPath;
import com.lithium.flow.filer.S3Filer;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.util.Caches;
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class S3JarProvider implements JarProvider {
	private static final Logger log = Logs.getLogger();

	private final Filer srcFiler;
	private final String s3Http;

	private final LoadingCache<String, String> cache;

	public S3JarProvider(@Nonnull Config config, @Nonnull Access access, @Nonnull Filer srcFiler) {
		checkNotNull(config);
		checkNotNull(access);
		this.srcFiler = checkNotNull(srcFiler);

		s3Http = config.getString("s3.http");

		Filer s3Filer = new S3Filer(config.prefix("s3"), access);

		cache = Caches.build(path -> {
			String name = RecordPath.getName(path);
			String s3Path = "/lib/" + name;

			Record srcRecord = srcFiler.getRecord(path);
			Record s3Record = s3Filer.getRecord(s3Path);

			if (srcRecord.getSize() != s3Record.getSize()) {
				log.debug("s3 upload: {}", path);
				srcFiler.copy(path, s3Filer, s3Path);
			}
			return s3Path;
		});
	}

	@Override
	public boolean copy(@Nonnull String path, @Nonnull Shell shell, @Nonnull Filer destFiler, @Nonnull String destDir)
			throws IOException {
		String name = RecordPath.getName(path);
		String destPath = destDir + "/" + name;

		Record srcRecord = srcFiler.getRecord(path);
		long srcSize = srcRecord.getSize();
		long destSize = destFiler.getRecord(destPath).getSize();
		if (srcSize != destSize) {
			log.debug("jar: ({} <-> {}) {} -> {}", srcSize, destSize, path, destPath);

			String s3Path;
			try {
				s3Path = cache.get(path);
			} catch (ExecutionException e) {
				log.warn("s3 upload failed: {}", path);
				return false;
			}

			String libPath = destDir + "/" + name;
			String command = "curl -sL " + s3Http + s3Path + " > " + libPath;

			log.debug("s3 download: {}{}", destFiler.getUri(), destPath);
			shell.exec(command).exit();
		}

		return srcSize == destFiler.getRecord(destPath).getSize();
	}
}
