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

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import org.apache.commons.io.IOUtils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;

/**
 * @author Matt Ayres
 */
public class HashEncoder {
	private final HashFunction function;
	private final BaseEncoding encoding;

	public HashEncoder(@Nonnull HashFunction function, @Nonnull BaseEncoding encoding) {
		this.function = checkNotNull(function);
		this.encoding = checkNotNull(encoding);
	}

	@Nonnull
	public String process(@Nonnull InputStream in) throws IOException {
		checkNotNull(in);
		try {
			HashingInputStream hashIn = new HashingInputStream(function, in);
			IOUtils.copy(hashIn, ByteStreams.nullOutputStream());
			return encoding.encode(hashIn.hash().asBytes());
		} finally {
			in.close();
		}
	}
}
