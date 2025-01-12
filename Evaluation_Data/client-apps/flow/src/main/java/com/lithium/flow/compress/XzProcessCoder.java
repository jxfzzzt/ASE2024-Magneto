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
import java.io.OutputStream;

import javax.annotation.Nonnull;

import org.tukaani.xz.LZMA2Options;

import com.google.common.collect.Lists;

/**
 * @author Matt Ayres
 */
public class XzProcessCoder extends ProcessCoder {
	public XzProcessCoder() {
		super(".xz", Lists.newArrayList("xz", "-d"), Lists.newArrayList("xz", "-{option}"));
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out) throws IOException {
		return wrapOut(out, LZMA2Options.PRESET_DEFAULT);
	}

	@Override
	@Nonnull
	public OutputStream wrapOut(@Nonnull OutputStream out, int option) throws IOException {
		return super.wrapOut(out, option == -1 ? LZMA2Options.PRESET_DEFAULT : option);
	}
}
