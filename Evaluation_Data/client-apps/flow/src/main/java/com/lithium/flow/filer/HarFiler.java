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

import com.lithium.flow.io.DataIo;
import com.lithium.flow.util.Caches;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class HarFiler implements Filer {
	private final Filer hdfsFiler;
	private final LoadingCache<String, Filer> harFilers;
	private final Function<String, String> harFunction;

	public HarFiler(@Nonnull Configuration conf) throws IOException {
		this(conf, path -> {
			if (path.endsWith(".har")) {
				return path;
			}

			int index = path.indexOf(".har/");
			if (index > -1) {
				return path.substring(0, index + 4);
			}

			return null;
		});
	}

	public HarFiler(@Nonnull Configuration conf, @Nonnull Function<String, String> harFunction) throws IOException {
		checkNotNull(conf);
		this.harFunction = checkNotNull(harFunction);

		Configuration hdfsConf = new Configuration(conf);
		hdfsConf.set("fs.har.impl.disable.cache", "true");
		String url = conf.get("fs.defaultFS");
		if (url != null) {
			hdfsConf.set("fs.defaultFS", url.replace("har://", "hdfs://"));
		}

		hdfsFiler = new HdfsFiler(hdfsConf);

		harFilers = Caches.build(path -> new HdfsFiler(FileSystem.get(URI.create("har://" + path), hdfsConf)));
	}

	@Override
	@Nonnull
	public URI getUri() throws IOException {
		return hdfsFiler.getUri();
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		return getFiler(path).listRecords(path);
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		return getFiler(path).getRecord(path);
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		return getFiler(path).readFile(path);
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		checkNotHarPath(path);
		return hdfsFiler.writeFile(path);
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		checkNotHarPath(path);
		return hdfsFiler.appendFile(path);
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) {
		throw new UnsupportedOperationException("openFile not implemented yet");
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		checkNotHarPath(path);
		hdfsFiler.setFileTime(path, time);
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		checkNotHarPath(path);
		hdfsFiler.deleteFile(path);
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		checkNotHarPath(oldPath);
		checkNotHarPath(newPath);
		hdfsFiler.renameFile(oldPath, newPath);
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		checkNotHarPath(path);
		hdfsFiler.createDirs(path);
	}

	@Override
	public void deleteDir(@Nonnull String path) throws IOException {
		deleteFile(path);
	}

	@Override
	public void close() throws IOException {
		hdfsFiler.close();
	}

	@Nonnull
	private Filer getFiler(@Nonnull String path) {
		checkNotNull(path);
		String harPath = harFunction.apply(path);
		return harPath != null ? harFilers.getUnchecked(harPath) : hdfsFiler;
	}

	private void checkNotHarPath(@Nonnull String path) throws IOException {
		if (harFunction.apply(path) != null) {
			throw new IOException("har paths are read only");
		}
	}
}
