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

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.table.ElasticRestTable;
import com.lithium.flow.table.Table;

import java.util.List;

import javax.annotation.Nonnull;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
public class ElasticRestUtils {
	@Nonnull
	public static RestHighLevelClient buildClient(@Nonnull Config config) {
		checkNotNull(config);

		List<String> hosts = config.getList("elastic.hosts", Splitter.on(' '));
		int port = config.getInt("elastic.port", 9200);

		HttpHost[] httpHosts = HostUtils.expand(hosts).stream()
				.map(host -> new HttpHost(host, port))
				.toArray(HttpHost[]::new);

		RestClientBuilder builder = RestClient.builder(httpHosts);

		if (config.containsKey("elastic.user")) {
			String user = config.getString("elastic.user");
			String password = config.getString("elastic.password");
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
			builder.setHttpClientConfigCallback(b -> b.setDefaultCredentialsProvider(credentialsProvider));
		}

		return new RestHighLevelClient(builder);
	}

	@Nonnull
	public static Table buildTable(@Nonnull Config config) {
		checkNotNull(config);

		return buildTable(config, buildClient(config));
	}

	@Nonnull
	public static Table buildTable(@Nonnull Config config, @Nonnull RestHighLevelClient client) {
		checkNotNull(config);
		checkNotNull(client);

		String index = config.getString("elastic.index");
		return new ElasticRestTable(client, index);
	}
}
