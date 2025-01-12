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

package com.lithium.flow.shell.cache;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.shell.Tunnel;
import com.lithium.flow.util.Reusable;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class DisposableTunnel implements Tunnel {
	private final Reusable<Tunnel> reusable;

	public DisposableTunnel(@Nonnull Reusable<Tunnel> reusable) {
		this.reusable = checkNotNull(reusable);
	}

	@Override
	@Nonnull
	public String getHost() {
		return reusable.get(this).getHost();
	}

	@Override
	public int getPort() {
		return reusable.get(this).getPort();
	}

	@Override
	public void close() {
		reusable.recycle(this);
	}
}
