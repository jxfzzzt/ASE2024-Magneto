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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;

import com.lithium.flow.config.Config;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.shell.Shore;
import com.lithium.flow.util.BaseEncodings;

import java.io.IOException;
import java.net.URI;

import javax.annotation.Nonnull;

/**
 * Remote shell implementation of {@link Filer}.
 *
 * @author Matt Ayres
 */
public class ShellFiler extends DecoratedFiler {
	private final Shell shell;

	public ShellFiler(@Nonnull Shell shell) throws IOException {
		super(checkNotNull(shell).getFiler());
		this.shell = shell;
	}

	public ShellFiler(@Nonnull Config config, @Nonnull Shore shore) throws IOException {
		this(buildShell(config, shore));
	}

	@Nonnull
	private static Shell buildShell(@Nonnull Config config, @Nonnull Shore shore) throws IOException {
		checkNotNull(config);
		checkNotNull(shore);
		URI uri = URI.create(config.getString("url"));
		return shore.getShell(uri.getHost());
	}

	@Override
	@Nonnull
	public String getHash(@Nonnull String path, @Nonnull String hash, @Nonnull String base) throws IOException {
		switch (hash) {
			case "md5":
			case "sha1":
			case "sha256":
			case "sha512":
				String command = hash + "sum '" + path + "' | awk '{ print $1 }'";
				String result = shell.exec(command).line().toUpperCase();
				return BaseEncodings.of(base).encode(base16().decode(result));
			default:
				return super.getHash(path, hash, base);
		}
	}

	@Override
	public void close() throws IOException {
		shell.close();
	}
}
