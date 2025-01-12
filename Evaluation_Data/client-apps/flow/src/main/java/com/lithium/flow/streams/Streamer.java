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

package com.lithium.flow.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface for implementations to apply input and output stream filtering.
 *
 * @author Matt Ayres
 */
public interface Streamer {
	/**
	 * @param out the output to be manipulated, cannot be {@code null}.
	 * @param name the name of stream, can be {@code null}.
	 * @return the filtered output, never {@code null}.
	 * @throws java.io.IOException if the filter initialization failed
	 */
	@Nonnull
	OutputStream filterOut(@Nonnull OutputStream out, @Nullable String name) throws IOException;

	/**
	 * @param in the input to be manipulated, cannot be {@code null}.
	 * @param name the name of stream, can be {@code null}.
	 * @return the filtered input, never {@code null}.
	 * @throws java.io.IOException if the filter initialization failed
	 */
	@Nonnull
	InputStream filterIn(@Nonnull InputStream in, @Nullable String name) throws IOException;
}
