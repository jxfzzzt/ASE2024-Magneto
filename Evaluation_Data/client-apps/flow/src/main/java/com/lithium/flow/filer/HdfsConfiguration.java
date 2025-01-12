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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.ioc.Locator;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 * @author Matt Ayres
 */
public class HdfsConfiguration extends Configuration {
	public HdfsConfiguration(@Nonnull Locator locator) {
		this(checkNotNull(locator).getInstance(Config.class));
	}

	public HdfsConfiguration(@Nonnull Config config) {
		checkNotNull(config);

		set("fs.defaultFS", config.getString("url"));

		for (String key : config.getPrefixKeys("hdfs")) {
			set(key.replaceAll("^hdfs.", ""), config.getString(key));
		}

		for (String resource : config.getList("hdfs.confs", Configs.emptyList())) {
			if (!new File(resource).exists()) {
				throw new IllegalArgumentException("resource from config 'hdfs.confs' doesn't exist: " + resource);
			}
			addResource(new Path(resource));
		}

		if (config.containsKey("hdfs.user.name")) {
			System.setProperty("HADOOP_USER_NAME", config.getString("hdfs.user.name"));
		}
	}
}
