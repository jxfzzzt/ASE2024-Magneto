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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.key.KeySource;
import com.lithium.flow.replacer.NoOpStringReplacer;
import com.lithium.flow.replacer.RegexStringReplacer;
import com.lithium.flow.replacer.StringReplacer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

/**
 * Filter to apply encryption on output and decryption on input.
 *
 * @author Matt Ayres
 */
public class CryptStreamer implements Streamer {
	private final KeySource keySource;
	private final byte[] header;
	private final String cipherName;
	private final StringReplacer replacer;

	public CryptStreamer(@Nonnull Config config, @Nonnull KeySource keySource) {
		checkNotNull(config);
		checkNotNull(keySource);

		header = config.getString("crypt.header", "LiAESv01").getBytes();
		cipherName = config.getString("crypt.cipher", "AES/CBC/PKCS5Padding");
		if (config.containsKey("crypt.regex")) {
			replacer = new RegexStringReplacer(config.getString("crypt.regex"),
					config.getString("crypt.replacement", "$1"));
		} else {
			replacer = new NoOpStringReplacer();
		}

		this.keySource = keySource;
	}

	@Override
	@Nonnull
	public OutputStream filterOut(@Nonnull OutputStream out, String name) throws IOException {
		try {
			for (Key key : keySource.getKeys(replacer.replace(name))) {
				out = encryptOut(out, key);
			}
		} catch (GeneralSecurityException e) {
			throw new IOException("encrypt failed", e);
		}
		return out;
	}

	@Nonnull
	private OutputStream encryptOut(@Nonnull OutputStream out, @Nonnull Key key)
			throws IOException, GeneralSecurityException {
		out.write(header);

		Cipher cipher = Cipher.getInstance(cipherName);
		byte[] iv = new byte[cipher.getBlockSize()];
		new SecureRandom().nextBytes(iv);
		AlgorithmParameterSpec spec = new IvParameterSpec(iv);

		out.write(iv);

		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		return new CipherOutputStream(out, cipher);
	}

	@Override
	@Nonnull
	public InputStream filterIn(@Nonnull InputStream in, @Nullable String name) throws IOException {
		checkArgument(name != null, "name must be specified for key lookup");

		try {
			for (Key key : keySource.getKeys(replacer.replace(name))) {
				in = decryptIn(in, key);
			}
		} catch (GeneralSecurityException e) {
			throw new IOException("decrypt failed", e);
		}
		return in;
	}

	@Nonnull
	private InputStream decryptIn(@Nonnull InputStream in, @Nonnull Key key)
			throws IOException, GeneralSecurityException {
		byte[] inHeader = new byte[header.length];
		if (in.read(inHeader) != inHeader.length) {
			throw new IOException("read didn't complete for header");
		}

		if (!Arrays.equals(header, inHeader)) {
			throw new IOException("unexpected header: '" + new String(inHeader) + "', expected: '"
					+ new String(header) + "'");
		}

		Cipher cipher = Cipher.getInstance(cipherName);
		byte[] iv = new byte[cipher.getBlockSize()];
		if (in.read(iv) != iv.length) {
			throw new IOException("read didn't complete for iv");
		}
		AlgorithmParameterSpec spec = new IvParameterSpec(iv);

		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		return new CipherInputStream(in, cipher);
	}
}
