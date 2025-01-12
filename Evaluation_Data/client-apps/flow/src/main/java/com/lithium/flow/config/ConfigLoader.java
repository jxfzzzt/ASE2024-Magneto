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

package com.lithium.flow.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Reads config file contents.
 *
 * @author Matt Ayres
 */
public interface ConfigLoader {
	/**
	 * Get an {@link java.io.InputStream} of a resource path for this loader. The caller is responsible for
	 * closing the stream. It's up to the implementation to determine if the path is absolute or relative.
	 *
	 * @param path the resource path to read data from, never {@code null}.
	 * @return the stream if the path exists for this loader, otherwise {@code null}.
	 * @throws java.io.IOException if there's an exception reading from the path.
	 */
	@Nullable
	InputStream getInputStream(@Nonnull String path) throws IOException;
}
