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

import org.anarres.lzo.LzopInputStream;
import org.anarres.lzo.hadoop.codec.LzoCompressor;
import org.anarres.lzo.hadoop.codec.LzopOutputStream;

/**
 * @author Matt Ayres
 */
public class LzopCoder implements Coder {
	@Override
	@Nonnull
	public InputStream wrapIn(@Nonnull InputStream in) throws IOException {
		return new LzopInputStream(in);
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out, int option) throws IOException {
		return new LzopOutputStream(out, LzoCompressor.CompressionStrategy.LZO1X_1, option);
	}

	@Override
	@Nonnull
	public String getExtension() {
		return ".lzo";
	}
}
