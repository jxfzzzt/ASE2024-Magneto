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
import static java.util.stream.Collectors.toList;

import com.lithium.flow.config.Config;
import com.lithium.flow.table.ElasticTable;
import com.lithium.flow.table.Table;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;

import com.google.common.base.Splitter;

/**
 * @author Matt Ayres
 */
public class ElasticUtils {
	private static final Logger log = Logs.getLogger();

	@Nonnull
	public static Client buildClient(@Nonnull Config config) {
		checkNotNull(config);

		String name = config.getString("elastic.name");
		Settings.Builder settings = Settings.builder();
		settings.put("cluster.name", name);

		config.keySet().stream()
				.filter(key -> key.startsWith("elastic:"))
				.forEach(key -> settings.put(key.replaceFirst("^elastic:", ""), config.getString(key)));

		List<Class<? extends Plugin>> plugins = config.getList("elastic.plugins", Collections.emptyList()).stream()
				.map(ElasticUtils::pluginClass)
				.collect(toList());

		@SuppressWarnings("deprecation")
		TransportClient client = new PreBuiltTransportClient(settings.build(), plugins);

		List<String> hosts = config.getList("elastic.hosts", Splitter.on(' '));
		int port = config.getInt("elastic.port", 9300);

		for (String host : HostUtils.expand(hosts)) {
			log.debug("adding host: {}", host);
			InetAddress address = Unchecked.get(() -> InetAddress.getByName(host));
			client.addTransportAddress(new TransportAddress(address, port));
		}

		return client;
	}

	@Nonnull
	public static Table buildTable(@Nonnull Config config) {
		checkNotNull(config);

		return buildTable(config, buildClient(config));
	}

	@Nonnull
	public static Table buildTable(@Nonnull Config config, @Nonnull Client client) {
		checkNotNull(config);
		checkNotNull(client);

		String index = config.getString("elastic.index");
		return new ElasticTable(client, index);
	}

	@Nonnull
	@SuppressWarnings("unchecked")
	private static Class<Plugin> pluginClass(@Nonnull String className) {
		try {
			return (Class<Plugin>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
