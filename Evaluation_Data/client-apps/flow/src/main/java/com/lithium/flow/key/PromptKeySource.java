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

package com.lithium.flow.key;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;

import com.lithium.flow.access.Prompt;

import java.security.Key;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Matt Ayres
 */
public class PromptKeySource implements KeySource {
	private final Prompt prompt;
	private final String promptName;

	public PromptKeySource(@Nonnull Prompt prompt, @Nonnull String promptName) {
		this.prompt = checkNotNull(prompt);
		this.promptName = checkNotNull(promptName);
	}

	@Override
	@Nonnull
	public List<Key> getKeys(@Nonnull String name) {
		checkNotNull(name);
		String hexKey = prompt.prompt(promptName, promptName + " key: ", Prompt.Type.MASKED).accept();
		return Collections.singletonList(new SecretKeySpec(base16().decode(hexKey), "AES"));
	}
}
