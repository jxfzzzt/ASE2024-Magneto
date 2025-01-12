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

import com.lithium.flow.filer.Filer;
import com.lithium.flow.shell.Shell;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class NoJarProvider implements JarProvider {
	@Override
	public boolean copy(@Nonnull String path, @Nonnull Shell shell, @Nonnull Filer destFiler, @Nonnull String destDir) {
		return false;
	}
}
