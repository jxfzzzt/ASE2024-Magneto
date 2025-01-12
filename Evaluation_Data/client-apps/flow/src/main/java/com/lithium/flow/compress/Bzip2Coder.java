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

package com.lithium.flow.compress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

/**
 * @author Matt Ayres
 */
public class Bzip2Coder implements Coder {
	@Override
	@Nonnull
	public InputStream wrapIn(@Nonnull InputStream in) throws IOException {
		return new BZip2CompressorInputStream(in);
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out, int option) throws IOException {
		return option == -1 ? new BZip2CompressorOutputStream(out) : new BZip2CompressorOutputStream(out, option);
	}

	@Override
	@Nonnull
	public String getExtension() {
		return ".bz2";
	}
}
