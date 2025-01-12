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

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Prompt;
import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.RecordPath;
import com.lithium.flow.store.MemoryStore;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Passwords;
import com.lithium.flow.vault.SecureVault;
import com.lithium.flow.vault.Vault;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Matt Ayres
 */
public class VaultRun {
	private static final Logger log = Logs.getLogger();

	private final Config runnerConfig;
	private final Map<String, String> map = new LinkedHashMap<>();
	private final String password = Passwords.create(34);

	private String env;

	public VaultRun(@Nonnull Vault vault, @Nonnull Prompt prompt, @Nonnull Config runnerConfig) {
		this.runnerConfig = checkNotNull(runnerConfig);

		Vault memoryVault = new SecureVault(Configs.empty(), new MemoryStore(map));
		memoryVault.setup(password);

		for (String name : runnerConfig.getList("vault.shells", Configs.emptyList())) {
			if (!name.contains("@")) {
				name = System.getProperty("user.name") + "@" + name;
			}

			prompt.prompt(name, name, Prompt.Type.MASKED).value();
			memoryVault.putValue(name, vault.getValue(name));
		}

		for (String name : runnerConfig.getList("vault.keys", Configs.emptyList())) {
			prompt.prompt(name, name, Prompt.Type.BLOCK).value();
			memoryVault.putValue(name, vault.getValue(name));
		}

		for (String name : runnerConfig.getList("vault.secrets", Configs.emptyList())) {
			prompt.prompt(name, name, Prompt.Type.MASKED).value();
			memoryVault.putValue(name, vault.getValue(name));
		}
	}

	public void deploy(@Nonnull Filer destFiler) throws IOException {
		ObjectMapper mapper = new ObjectMapper().enable(INDENT_OUTPUT);
		String vaultOut = runnerConfig.getString("vault.out", "");

		if (vaultOut.length() > 0) {
			destFiler.createDirs(RecordPath.getFolder(vaultOut));
			try (OutputStream out = destFiler.writeFile(vaultOut)) {
				mapper.writeValue(out, map);
			}
			log.debug("wrote: {}", vaultOut);

			env = "export VAULT_PASSWORD=" + password;
		}
	}

	@Nullable
	public String getEnv() {
		return env;
	}
}
