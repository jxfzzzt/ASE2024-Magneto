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

package com.lithium.flow.config.exception;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public class IllegalConfigException extends RuntimeException {
	private final String key;
	private final String value;
	private final String type;

	public IllegalConfigException(@Nonnull String key) {
		this(key, null, null, null);
	}

	public IllegalConfigException(@Nonnull String key, @Nullable String value, @Nullable String type,
			@Nullable Throwable cause) {
		super(cause);
		this.key = checkNotNull(key);
		this.value = value;
		this.type = type;
	}

	@Nonnull
	public String getKey() {
		return key;
	}

	@Nullable
	public String getValue() {
		return value;
	}

	@Nullable
	public String getType() {
		return type;
	}

	@Override
	public String getMessage() {
		return value == null ? String.format("config key '%s' not set", key) :
				String.format("config key '%s' has invalid %s value: %s", key, type, value);
	}
}
