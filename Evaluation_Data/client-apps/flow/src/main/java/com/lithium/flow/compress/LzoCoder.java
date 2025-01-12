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

import org.anarres.lzo.LzoDecompressor1x;
import org.anarres.lzo.LzoInputStream;
import org.anarres.lzo.hadoop.codec.LzoCompressor;
import org.apache.hadoop.conf.Configuration;

import com.hadoop.compression.lzo.LzoCodec;

/**
 * @author Matt Ayres
 */
public class LzoCoder implements Coder {
	@Override
	@Nonnull
	public InputStream wrapIn(@Nonnull InputStream in) {
		return new LzoInputStream(in, new LzoDecompressor1x());
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out, int option) throws IOException {
		Configuration conf = new Configuration();
		LzoCodec.setCompressionStrategy(conf, LzoCompressor.CompressionStrategy.LZO1X_1);
		if (option != -1) {
			LzoCodec.setBufferSize(conf, option);
		}

		LzoCodec codec = new LzoCodec();
		codec.setConf(conf);
		return codec.createOutputStream(out);
	}

	@Override
	@Nonnull
	public String getExtension() {
		return ".lzo_deflate";
	}
}
