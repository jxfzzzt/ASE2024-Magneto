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
import com.lithium.flow.util.Logs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class LogFiler extends DecoratedFiler {
	private static final Logger log = Logs.getLogger();

	private final Filer delegate;
	private final boolean enter;
	private final boolean exit;

	public LogFiler(@Nonnull Filer delegate) {
		this(delegate, true, true);
	}

	public LogFiler(@Nonnull Filer delegate, boolean enter, boolean exit) {
		super(delegate);
		this.delegate = checkNotNull(delegate);
		this.enter = enter;
		this.exit = exit;
	}

	@Override
	@Nonnull
	public URI getUri() throws IOException {
		if (enter) {
			log.info("enter: getUri()");
		}
		try {
			return delegate.getUri();
		} finally {
			if (exit) {
				log.info("exit: getUri()");
			}
		}
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		if (enter) {
			log.info("enter: listRecords(\"{}\")", path);
		}
		try {
			return delegate.listRecords(path);
		} finally {
			if (exit) {
				log.info("exit: listRecords(\"{}\")", path);
			}
		}
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		if (enter) {
			log.info("enter: getRecord(\"{}\")", path);
		}
		try {
			return delegate.getRecord(path);
		} finally {
			if (exit) {
				log.info("exit: getRecord(\"{}\")", path);
			}
		}
	}

	@Override
	@Nonnull
	public Stream<Record> findRecords(@Nonnull String path, int threads) throws IOException {
		if (enter) {
			log.info("enter: findRecords(\"{}\", {})", path, threads);
		}
		try {
			return delegate.findRecords(path, threads);
		} finally {
			if (exit) {
				log.info("exit: findRecords(\"{}\", {})", path, threads);
			}
		}
	}

	@Override
	@Nonnull
	public String getHash(@Nonnull String path, @Nonnull String hash, @Nonnull String base) throws IOException {
		if (enter) {
			log.info("enter: getHash(\"{}\", \"{}\", \"{}\")", path, hash, base);
		}
		try {
			return super.getHash(path, hash, base);
		} finally {
			if (exit) {
				log.info("exit: getHash(\"{}\", \"{}\", \"{}\")", path, hash, base);
			}
		}
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		if (enter) {
			log.info("enter: readFile(\"{}\")", path);
		}
		try {
			return delegate.readFile(path);
		} finally {
			if (exit) {
				log.info("exit: readFile(\"{}\")", path);
			}
		}
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		if (enter) {
			log.info("enter: writeFile(\"{}\")", path);
		}
		try {
			return delegate.writeFile(path);
		} finally {
			if (exit) {
				log.info("exit: writeFile(\"{}\")", path);
			}
		}
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		if (enter) {
			log.info("enter: appendFile(\"{}\")", path);
		}
		try {
			return delegate.appendFile(path);
		} finally {
			if (exit) {
				log.info("exit: appendFile(\"{}\")", path);
			}
		}
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) throws IOException {
		if (enter) {
			log.info("enter: openFile(\"{}\")", path);
		}
		try {
			return delegate.openFile(path, write);
		} finally {
			if (exit) {
				log.info("exit: openFile(\"{}\")", path);
			}
		}
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		if (enter) {
			log.info("enter: setFileTime(\"{}\", {})", path, time);
		}
		try {
			delegate.setFileTime(path, time);
		} finally {
			if (exit) {
				log.info("exit: setFileTime(\"{}\", {})", path, time);
			}
		}
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		if (enter) {
			log.info("enter: deleteFile(\"{}\")", path);
		}
		try {
			delegate.deleteFile(path);
		} finally {
			if (exit) {
				log.info("exit: deleteFile(\"{}\")", path);
			}
		}
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		if (enter) {
			log.info("enter: createDirs(\"{}\")", path);
		}
		try {
			delegate.createDirs(path);
		} finally {
			if (exit) {
				log.info("exit: createDirs(\"{}\")", path);
			}
		}
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		if (enter) {
			log.info("enter: renameFile(\"{}\", \"{}\")", oldPath, newPath);
		}
		try {
			delegate.renameFile(oldPath, newPath);
		} finally {
			if (exit) {
				log.info("exit: renameFile(\"{}\", \"{}\")", oldPath, newPath);
			}
		}
	}

	@Override
	public void close() throws IOException {
		if (enter) {
			log.info("enter: close()");
		}
		try {
			delegate.close();
		} finally {
			if (exit) {
				log.info("exit: close()");
			}
		}
	}
}
