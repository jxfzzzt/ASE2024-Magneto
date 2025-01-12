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

import javax.annotation.Nonnull;

/**
 * Defines what to do with a line of a config file.
 *
 * @author Matt Ayres
 */
public interface ConfigParser {
	/**
	 * Parse a single line of a config file.
	 *
	 * @param line    the line to be evaluated, never {@code null}.
	 * @param builder the builder to make changes to, never @{code null}.
	 * @return true if the parser used the line, to indicate that it shouldn't be parsed again.
	 * @throws java.io.IOException if the consequence of consuming the line had a problem
	 */
	boolean parseLine(@Nonnull String line, @Nonnull ConfigBuilder builder) throws IOException;
}
