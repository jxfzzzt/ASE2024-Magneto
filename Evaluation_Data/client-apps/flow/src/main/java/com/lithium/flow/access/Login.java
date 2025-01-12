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

package com.lithium.flow.access;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Matt Ayres
 */
public class Login {
	private static final int NO_PORT = -1;

	private final String user;
	private final String host;
	private final int port;
	private final String keyPath;

	public Login(@Nonnull String user, @Nonnull String host, int port, @Nullable String keyPath) {
		this.user = checkNotNull(user);
		this.host = checkNotNull(host);
		this.port = port;
		this.keyPath = keyPath;
	}

	@Nonnull
	public String getUser() {
		return user;
	}

	@Nonnull
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getPortOrDefault(int def) {
		return port != NO_PORT ? port : def;
	}

	@Nullable
	public String getKeyPath() {
		return keyPath;
	}

	@Nonnull
	public String getHostAndPort() {
		return host + (port == NO_PORT ? "" : ":" + port);
	}

	@Nonnull
	public String getDisplayString() {
		return user + "@" + getHostAndPort();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Login login = (Login) o;
		return port == login.port
				&& Objects.equals(user, login.user)
				&& Objects.equals(host, login.host)
				&& Objects.equals(keyPath, login.keyPath);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, host, port, keyPath);
	}

	@Override
	@Nonnull
	public String toString() {
		return getDisplayString();
	}

	@Nonnull
	public Builder toBuilder() {
		return new Builder().setUser(user).setHost(host).setPort(port).setKeyPath(keyPath);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@Nonnull
	public static Login from(@Nonnull String spec) {
		String host = spec;
		String user = System.getProperty("user.name");
		int port = NO_PORT;

		int index = host.indexOf('@');
		if (index > -1) {
			user = host.substring(0, index);
			host = host.substring(index + 1);
		}
		index = host.indexOf(':');
		if (index > -1) {
			port = Integer.parseInt(host.substring(index + 1));
			host = host.substring(0, index);
		}

		return Login.newBuilder().setUser(user).setHost(host).setPort(port).build();
	}

	public static class Builder {
		private String user;
		private String host;
		private int port = NO_PORT;
		private String keyPath;

		@Nonnull
		public Builder setUser(@Nonnull String user) {
			this.user = checkNotNull(user);
			return this;
		}

		@Nonnull
		public Builder setHost(@Nonnull String host) {
			this.host = checkNotNull(host);
			return this;
		}

		@Nonnull
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}

		@Nonnull
		public Builder setKeyPath(@Nullable String keyPath) {
			this.keyPath = keyPath;
			return this;
		}

		@Nonnull
		public Login build() {
			return new Login(user, host, port, keyPath);
		}
	}
}
