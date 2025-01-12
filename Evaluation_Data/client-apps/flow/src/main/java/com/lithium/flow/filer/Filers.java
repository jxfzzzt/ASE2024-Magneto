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

import com.lithium.flow.access.Access;
import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;
import com.lithium.flow.config.repos.Repos;
import com.lithium.flow.filer.chain.CachedFilerChain;
import com.lithium.flow.filer.chain.ReadOnlyFilerChain;
import com.lithium.flow.filer.chain.SubpathsFilerChain;
import com.lithium.flow.filer.chain.TempWriterFilerChain;
import com.lithium.flow.filer.hash.HashFilerChain;
import com.lithium.flow.filer.lucene.LuceneFilerChain;
import com.lithium.flow.filer.remote.ClientRemoteFiler;
import com.lithium.flow.key.KeySource;
import com.lithium.flow.key.Keys;
import com.lithium.flow.shell.Shells;
import com.lithium.flow.shell.Shore;
import com.lithium.flow.streams.BufferedStreamer;
import com.lithium.flow.streams.CompressStreamer;
import com.lithium.flow.streams.CryptStreamer;
import com.lithium.flow.vault.Vault;
import com.lithium.flow.vault.Vaults;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.hadoop.conf.Configuration;

/**
 * @author Matt Ayres
 */
public class Filers {
	private static final FilerFactory factory = buildFactory();

	@Nonnull
	public static FilerFactory buildFactory() {
		return new FilerFactory()
				.addScheme("file", LocalFiler.class)
				.addScheme("har", HarFiler.class)
				.addScheme("hdfs", HdfsFiler.class)
				.addScheme("rmi", ClientRemoteFiler.class)
				.addScheme("ssh", ShellFiler.class)
				.addScheme("sftp", ShellFiler.class)
				.addScheme("s3", S3Filer.class)
				.addStreamer("buffered", BufferedStreamer.class)
				.addStreamer("compress", CompressStreamer.class)
				.addStreamer("crypt", CryptStreamer.class)
				.addChain("readonly", ReadOnlyFilerChain.class)
				.addChain("subpath", SubpathsFilerChain.class)
				.addChain("temp", TempWriterFilerChain.class)
				.addChain("cache", CachedFilerChain.class)
				.addChain("lucene", LuceneFilerChain.class)
				.addChain("hash", HashFilerChain.class)
				.attempt(ff -> ff.addProvider(Configuration.class, HdfsConfiguration::new))
				.attempt(ff -> ff.addProvider(Access.class, Vaults::buildAccess))
				.attempt(ff -> ff.addProvider(Vault.class, Vaults::buildVault))
				.attempt(ff -> ff.addProvider(Shore.class, Shells::buildShore))
				.attempt(ff -> ff.addProvider(KeySource.class, Keys::buildKeySource))
				.attempt(ff -> ff.addProvider(Repo.class, Repos::buildRepo));
	}

	@Nonnull
	public static Filer buildFiler(@Nonnull Config config, Object... provided) throws IOException {
		return factory.buildFiler(config, provided);
	}
}
