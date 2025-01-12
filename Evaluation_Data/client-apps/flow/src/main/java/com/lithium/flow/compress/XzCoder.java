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

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

/**
 * @author Matt Ayres
 */
public class XzCoder implements Coder {
	@Override
	@Nonnull
	public InputStream wrapIn(@Nonnull InputStream in) throws IOException {
		return new XZInputStream(in);
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out, int option) throws IOException {
		LZMA2Options options;
		if (option == -1) {
			options = new LZMA2Options();
		} else if (option < 10) {
			options = new LZMA2Options(option);
		} else {
			// extreme options from xz src/liblzma/lzma/lzma_encoder_presets.c
			options = new LZMA2Options(option - 10);
			options.setMode(LZMA2Options.MODE_NORMAL);
			options.setMatchFinder(LZMA2Options.MF_BT4);
			if (option == 13 || option == 15) {
				options.setNiceLen(192);
				options.setDepthLimit(0);
			} else {
				options.setNiceLen(273);
				options.setDepthLimit(512);
			}
		}
		return new XZOutputStream(out, options);
	}

	@Override
	@Nonnull
	public String getExtension() {
		return ".xz";
	}
}
