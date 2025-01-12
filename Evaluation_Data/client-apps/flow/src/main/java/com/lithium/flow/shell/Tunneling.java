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

package com.lithium.flow.shell;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Matt Ayres
 */
public class Tunneling {
	private final int listen;
	private final String host;
	private final int port;

	public Tunneling(int listen, @Nonnull String host, int port) {
		this.listen = listen;
		this.host = checkNotNull(host);
		this.port = port;
	}

	public int getListen() {
		return listen;
	}

	@Nonnull
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object that) {
		return that instanceof Tunneling && EqualsBuilder.reflectionEquals(this, that);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
