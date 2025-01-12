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

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.DecoratedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.io.Swallower;
import com.lithium.flow.jetty.JettyClient;
import com.lithium.flow.util.BaseEncodings;

import java.io.IOException;
import java.util.Scanner;

import javax.annotation.Nonnull;

import com.google.common.io.BaseEncoding;

/**
 * @author Matt Ayres
 */
public class ClientHashFiler extends DecoratedFiler {
	private final BaseEncoding encoding = BaseEncoding.base16().lowerCase();

	private final JettyClient client;

	public ClientHashFiler(@Nonnull Filer delegate, @Nonnull Config config) {
		super(delegate);
		client = new JettyClient(config.prefix("hash"));
	}

	@Override
	@Nonnull
	public String getHash(@Nonnull String path, @Nonnull String hash, @Nonnull String base) throws IOException {
		return client.call("hash " + path, input -> {
			Scanner scanner = new Scanner(input);
			scanner.next(); // size
			String value = scanner.next();
			scanner.next(); // status
			return BaseEncodings.of(base).encode(encoding.decode(value));
		});
	}

	@Override
	public void close() throws IOException {
		Swallower.close(client);
		super.close();
	}
}
