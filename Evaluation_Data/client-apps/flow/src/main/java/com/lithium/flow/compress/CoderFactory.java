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

package com.lithium.flow.compress;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.util.Lazy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class CoderFactory {
	private final Map<String, Lazy<Coder>> coders = new ConcurrentHashMap<>();
	private final Lazy<Coder> defaultCoder = new Lazy<>(() -> NoCoder.class.getConstructor().newInstance());

	@Nonnull
	public CoderFactory register(@Nonnull String extension, @Nonnull Class<? extends Coder> clazz) {
		checkNotNull(extension);
		checkNotNull(clazz);
		return register(extension, new Lazy<>(() -> clazz.getConstructor().newInstance()));
	}

	@Nonnull
	public CoderFactory register(@Nonnull String extension, @Nonnull Lazy<Coder> lazy) {
		checkNotNull(extension);
		checkNotNull(lazy);
		coders.put(extension, lazy);
		return this;
	}

	@Nonnull
	public Coder getCoder(@Nonnull String path) {
		checkNotNull(path);

		int index = path.lastIndexOf(".");
		String extension = index > -1 ? path.substring(index) : "." + path;
		return coders.getOrDefault(extension, defaultCoder).get();
	}
}
