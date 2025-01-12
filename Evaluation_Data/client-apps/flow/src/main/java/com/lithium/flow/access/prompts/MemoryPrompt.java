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

package com.lithium.flow.access.prompts;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Prompt;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class MemoryPrompt implements Prompt {
	private final Prompt delegate;
	private final Map<String, Response> map = new HashMap<>();

	public MemoryPrompt(@Nonnull Prompt delegate) {
		this.delegate = checkNotNull(delegate);
	}

	@Override
	@Nonnull
	public Response prompt(@Nonnull String name, @Nonnull String message, @Nonnull Type type) {
		Response response = map.computeIfAbsent(name, key -> delegate.prompt(name, message, type));
		return Response.build(response.value(), valid -> {
			if (valid) {
				response.accept();
				map.put(name, response);
			} else {
				response.reject();
				map.remove(name);
			}
		});
	}
}
