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

package com.lithium.flow.filer.hash;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.ioc.Chain;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class HashFilerChain implements Chain<Filer> {
	private final Config config;

	public HashFilerChain(@Nonnull Config config) {
		this.config = checkNotNull(config).prefix("hash");
	}

	@Override
	@Nonnull
	public Filer chain(@Nonnull Filer filer) {
		filer = new ClientHashFiler(filer, config);
		filer = new RedisHashFiler(filer, config);
		return filer;
	}
}
