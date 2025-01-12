/*
 * Copyright 2020 Lithium Technologies, Inc.
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
import com.lithium.flow.config.Configs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;

/**
 * @author Matt Ayres
 */
public class AmazonVault implements Vault {
	private final AWSSimpleSystemsManagement ssm;
	private final String prefix;
	private final Map<String, String> aliases = new HashMap<>();

	public AmazonVault(@Nonnull Config config) {
		checkNotNull(config);

		Config ssmConfig = config.prefix("ssm");
		AWSCredentialsProvider credentials = buildCredentials(ssmConfig);
		AWSSimpleSystemsManagementClientBuilder builder = AWSSimpleSystemsManagementClientBuilder.standard();
		builder.withCredentials(credentials);
		if (ssmConfig.containsKey("aws.region")) {
			builder.withRegion(ssmConfig.getString("aws.region"));
		}
		ssm = builder.build();
		prefix = config.getString("ssm.prefix", "");

		for (String alias : config.getList("ssm.aliases", Configs.emptyList())) {
			aliases.put(config.getString("ssm.alias." + alias), alias);
		}
	}

	@Override
	@Nonnull
	public State getState() {
		return State.UNLOCKED;
	}

	@Override
	public void setup(@Nonnull String password) {
	}

	@Override
	public boolean unlock(@Nonnull String password) {
		return true;
	}

	@Override
	public void lock() {
	}

	@Override
	public void putValue(@Nonnull String key, @Nullable String value) {
	}

	@Override
	@Nullable
	public String getValue(@Nonnull String key) {
		String name = prefix + aliases.getOrDefault(key, key);
		GetParameterRequest request = new GetParameterRequest().withName(name).withWithDecryption(true);
		GetParameterResult result = ssm.getParameter(request);
		return result.getParameter().getValue();
	}

	@Override
	@Nonnull
	public Set<String> getKeys() {
		return Collections.emptySet();
	}

	@Nonnull
	public static AWSCredentialsProvider buildCredentials(@Nonnull Config config) {
		String credentials = config.getString("aws.credentials", "instance");
		switch (credentials) {
			case "instance":
				return InstanceProfileCredentialsProvider.getInstance();

			case "profile":
				String profile = config.getString("aws.profile");
				return new ProfileCredentialsProvider(profile);

			default:
				throw new RuntimeException("unknown credentials provider: " + credentials);
		}
	}
}
