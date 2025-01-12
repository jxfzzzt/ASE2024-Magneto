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
import com.lithium.flow.shell.Shell;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public interface JarProvider {
	boolean copy(@Nonnull String path, @Nonnull Shell shell, @Nonnull Filer destFiler, @Nonnull String destDir)
			throws IOException;

	@Nonnull
	static JarProvider build(@Nonnull Config config, @Nonnull Access access, @Nonnull Filer filer) {
		checkNotNull(config);
		checkNotNull(access);
		checkNotNull(filer);

		switch (config.getString("jar.provider")) {
			case "s3":
				return new S3JarProvider(config, access, filer);
			case "maven":
				return new MavenJarProvider(config, filer);
			case "local":
				return new LocalJarProvider(filer);
			default:
				return new NoJarProvider();
		}
	}
}
