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

package com.lithium.flow.vault;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.store.Store;
import com.lithium.flow.util.Encodings;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.io.BaseEncoding;

/**
 * @author Matt Ayres
 */
public class SecureVault implements Vault {
	private final Config config;
	private final Store store;
	private BaseEncoding encoding;
	private String cipherValue;
	private SecretKey secretKey;

	public SecureVault(@Nonnull Config config, @Nonnull Store store) {
		this.config = checkNotNull(config);
		this.store = checkNotNull(store);
	}

	@Override
	@Nonnull
	public State getState() {
		String check = store.getValue("vault.check");
		return check == null ? State.NEW : secretKey == null ? State.LOCKED : State.UNLOCKED;
	}

	@Override
	public void setup(@Nonnull String password) {
		checkNotNull(password);

		State state = getState();
		switch (state) {
			case NEW:
				setPassword(password);
				break;

			case LOCKED:
				throw new VaultException("vault locked");

			case UNLOCKED:
				resetPassword(password);
				break;
		}
	}

	private void setPassword(@Nonnull String password) {
		store.putValue("vault.factory", config.getString("vault.factory", "PBKDF2WithHmacSHA512"));
		store.putValue("vault.cycles", config.getString("vault.cycles", "100000"));
		store.putValue("vault.algorithm", config.getString("vault.algorithm", "AES"));
		store.putValue("vault.cipher", config.getString("vault.cipher", "AES/CBC/PKCS5Padding"));
		store.putValue("vault.bits", config.getString("vault.bits", "128"));
		store.putValue("vault.encoding", config.getString("vault.encoding", "base64"));

		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		store.putValue("vault.salt", Encodings.get(getStoreValue("vault.encoding")).encode(salt));

		setSecretKey(password);

		try {
			store.putValue("vault.check", encrypt("check"));
		} catch (GeneralSecurityException e) {
			secretKey = null;
			throw new VaultException("setup failed", e);
		}
	}

	private void resetPassword(@Nonnull String password) {
		Map<String, String> secrets = new HashMap<>();
		for (String key : getKeys()) {
			secrets.put(key, getValue(key));
		}

		setPassword(password);

		for (String key : secrets.keySet()) {
			putValue(key, secrets.get(key));
		}
	}

	private void setSecretKey(@Nonnull String password) {
		encoding = Encodings.get(getStoreValue("vault.encoding"));
		cipherValue = getStoreValue("vault.cipher");

		byte[] salt = encoding.decode(getStoreValue("vault.salt"));
		int cycles = Integer.parseInt(getStoreValue("vault.cycles"));
		int bits = Integer.parseInt(getStoreValue("vault.bits"));

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, cycles, bits);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance(getStoreValue("vault.factory"));
			secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), getStoreValue("vault.algorithm"));
		} catch (GeneralSecurityException e) {
			secretKey = null;
			throw new VaultException("set secretKey failed", e);
		}
	}

	@Override
	public boolean unlock(@Nonnull String password) {
		String check = store.getValue("vault.check");
		if (check == null) {
			throw new VaultException("vault not setup");
		}

		setSecretKey(password);

		try {
			return decrypt(check).equals("check");
		} catch (InvalidKeyException e) {
			secretKey = null;
			throw new VaultException("invalid secretKey");
		} catch (GeneralSecurityException e) {
			secretKey = null;
			return false;
		}
	}

	@Override
	public void lock() {
		secretKey = null;
	}

	@Nonnull
	private String getStoreValue(@Nonnull String key) {
		String value = store.getValue(key);
		if (value == null) {
			throw new VaultException("no secretKey: " + key);
		}
		return value;
	}

	private void checkReady() {
		switch (getState()) {
			case NEW:
				throw new VaultException("vault not setup");
			case LOCKED:
				throw new VaultException("vault locked");
		}
	}

	@Override
	public void putValue(@Nonnull String key, @Nullable String secret) {
		checkReady();

		try {
			store.putValue(key, secret != null ? encrypt(secret) : null);
		} catch (GeneralSecurityException e) {
			throw new VaultException("put failed", e);
		}
	}

	@Override
	@Nullable
	public String getValue(@Nonnull String key) {
		checkReady();

		String encoded = store.getValue(key);
		if (encoded == null) {
			return null;
		}

		try {
			return decrypt(encoded);
		} catch (GeneralSecurityException e) {
			throw new VaultException("decode failed", e);
		}
	}

	@Override
	@Nonnull
	public Set<String> getKeys() {
		return store.getKeys().stream().filter(k -> !k.startsWith("vault."))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@Nonnull
	private String encrypt(@Nonnull String text) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(cipherValue);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		byte[] bytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
		return encoding.encode(iv) + "." + encoding.encode(bytes);
	}

	@Nonnull
	private String decrypt(@Nonnull String text) throws GeneralSecurityException {
		int index = text.indexOf(".");
		if (index == -1 || index == text.length() - 1) {
			throw new VaultException("encoded value has no delimiter: " + text);
		}

		byte[] iv = encoding.decode(text.substring(0, index));
		byte[] bytes = encoding.decode(text.substring(index + 1));

		Cipher cipher = Cipher.getInstance(cipherValue);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
	}
}
