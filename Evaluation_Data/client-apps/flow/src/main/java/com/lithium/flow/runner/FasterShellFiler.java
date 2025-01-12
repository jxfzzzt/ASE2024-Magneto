/*
 * Copyright 2017 Lithium Technologies, Inc.
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

package com.lithium.flow.runner;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;
import static java.util.stream.Collectors.toList;

import com.lithium.flow.filer.DecoratedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.RecordPath;
import com.lithium.flow.shell.Exec;
import com.lithium.flow.shell.Shell;
import com.lithium.flow.util.BaseEncodings;
import com.lithium.flow.util.CheckedSupplier;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class FasterShellFiler extends DecoratedFiler {
	private final CheckedSupplier<Shell, IOException> supplier;

	public FasterShellFiler(@Nonnull Filer filer, @Nonnull CheckedSupplier<Shell, IOException> supplier) {
		super(checkNotNull(filer));
		this.supplier = checkNotNull(supplier);
	}

	@Override
	@Nonnull
	public Stream<Record> findRecords(@Nonnull String path, int threads) throws IOException {
		try {
			String command = "find " + path + " -printf \"%P|%s|%T@|%y\\n\" | tail -n +2";
			URI uri = getUri();
			try (Exec exec = supplier.get().exec(command)) {
				return exec.out().filter(line -> line.endsWith("|d") || line.endsWith("|f")).map(line -> {
					Scanner scanner = new Scanner(line).useDelimiter("\\|");
					String subpath = scanner.next();
					long size = scanner.nextLong();
					long time = (long) (scanner.nextDouble() * 1000);
					boolean dir = scanner.next().equals("d");
					return new Record(uri, RecordPath.from(path + "/" + subpath), time, size, dir);
				}).collect(toList()).stream();
			}
		} catch (Exception e) {
			return super.findRecords(path, threads);
		}
	}

	@Override
	@Nonnull
	public String getHash(@Nonnull String path, @Nonnull String hash, @Nonnull String base) throws IOException {
		switch (hash) {
			case "md5":
			case "sha1":
			case "sha256":
			case "sha512":
				String command = hash + "sum '" + path + "' | awk '{ print $1 }'";
				String result = supplier.get().exec(command).line().toUpperCase();
				return BaseEncodings.of(base).encode(base16().decode(result));
			default:
				return super.getHash(path, hash, base);
		}
	}
}
