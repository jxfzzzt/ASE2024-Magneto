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

package com.lithium.flow.filer.chain;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.TempWriteFiler;
import com.lithium.flow.ioc.Chain;
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.net.InetAddress;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class TempWriterFilerChain implements Chain<Filer> {
	private static final Logger log = Logs.getLogger();

	private final Config config;

	public TempWriterFilerChain(@Nonnull Config config) {
		this.config = checkNotNull(config);
	}

	@Override
	@Nonnull
	public Filer chain(@Nonnull Filer input) throws IOException {
		String tempDirPath = config.getString("temp.dir");
		String tempExtension = config.getString("temp.extension", ".temp");
		boolean tempOverwrite = config.getBoolean("temp.overwrite", true);
		boolean tempCreate = config.getBoolean("temp.create", true);
		boolean tempDelete = config.getBoolean("temp.delete", true);

		if (tempDirPath.contains("{host}")) {
			tempDirPath = tempDirPath.replace("{host}", InetAddress.getLocalHost().getHostName());
		}

		Filer filer = new TempWriteFiler(input, tempDirPath, tempExtension, tempOverwrite);

		if (tempCreate) {
			filer.createDirs(tempDirPath);
		}

		if (tempDelete) {
			for (Record tempRecord : filer.listRecords(tempDirPath)) {
				if (tempRecord.getName().endsWith(tempExtension)) {
					log.info("deleting temp: {}", tempRecord);
					filer.deleteFile(tempRecord.getPath());
				}
			}
		}

		return filer;
	}
}
