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

package com.lithium.flow.svn;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Access;
import com.lithium.flow.access.Login;
import com.lithium.flow.access.Prompt.Response;
import com.lithium.flow.access.Prompt.Type;
import com.lithium.flow.config.Config;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.auth.SVNAuthentication;
import org.tmatesoft.svn.core.auth.SVNSSHAuthentication;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

/**
 * @author Matt Ayres
 */
public class LoginSvnProvider implements SvnProvider {
	private final Config config;
	private final Access access;
	private final SVNURL url;

	public LoginSvnProvider(@Nonnull Config config, @Nonnull Access access, @Nonnull String url) throws SVNException {
		this.config = checkNotNull(config);
		this.access = checkNotNull(access);
		this.url = SVNURL.parseURIEncoded(checkNotNull(url));
	}

	@Override
	@Nonnull
	public SVNURL getLocation() {
		return url;
	}

	@Override
	@Nonnull
	@SuppressWarnings("deprecation")
	public SVNRepository getRepository() throws IOException {
		SVNException exception = null;
		Login login = access.getLogin(url.getHost());
		String keyPath = login.getKeyPath();
		int retries = config.getInt("svn.retries", 3);

		for (int i = 0; i < retries + 1; i++) {
			ISVNAuthenticationManager authManager;
			Response key = Response.build("");
			Response pass;

			if (keyPath != null) {
				if (new File(keyPath).isFile()) {
					pass = prompt(keyPath, "Enter passphrase for {name}: ", Type.MASKED);
					authManager = new BasicAuthenticationManager(login.getUser(), new File(keyPath),
							pass.value(), login.getPortOrDefault(22));
				} else {
					key = prompt("key[" + keyPath + "]", "Enter private key for {name}: ", Type.BLOCK);
					pass = prompt("pass[" + keyPath + "]", "Enter passphrase for {name}: ", Type.MASKED);

					String user = login.getUser();
					char[] keyChars = key.value().toCharArray();
					char[] passChars = pass.value().isEmpty() ? null : pass.value().toCharArray();
					int port = login.getPortOrDefault(22);

					authManager = BasicAuthenticationManager.newInstance(new SVNAuthentication[] {
							SVNSSHAuthentication.newInstance(user, keyChars, passChars, port, false, null, false)
					});
				}
			} else {
				pass = prompt(login.getDisplayString(), "Enter password for {name}: ", Type.MASKED);
				authManager = new BasicAuthenticationManager(login.getUser(), pass.value());
			}

			try {
				SVNRepository repository = SVNRepositoryFactory.create(url);
				repository.setAuthenticationManager(authManager);
				repository.testConnection();
				key.accept();
				pass.accept();
				return repository;
			} catch (SVNAuthenticationException e) {
				exception = e;
				key.reject();
				pass.reject();
			} catch (SVNException e) {
				exception = e;
				break;
			}
		}

		throw new IOException("failed to created repository: " + url, exception);
	}

	@Override
	public void releaseRepository(@Nonnull SVNRepository repository) {
		repository.closeSession();
	}

	@Override
	public void close() {
	}

	@Nonnull
	private Response prompt(@Nonnull String name, @Nonnull String message, @Nonnull Type type) {
		return access.getPrompt().prompt(name, message.replace("{name}", name), type);
	}
}
