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

package com.lithium.flow.config.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Repo;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class DecoratedRepo implements Repo {
	private final Repo delegate;

	public DecoratedRepo(@Nonnull Repo delegate) {
		this.delegate = checkNotNull(delegate);
	}

	@Override
	@Nonnull
	public List<String> getNames() throws IOException {
		return delegate.getNames();
	}

	@Override
	@Nonnull
	public Config getConfig(@Nonnull String name) throws IOException {
		return delegate.getConfig(name);
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}
}
