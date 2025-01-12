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

package com.lithium.flow.io;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public abstract class AbstractDataIo implements DataIo {
	private final DataInput dataIn;
	private final DataOutput dataOut;

	public AbstractDataIo(@Nonnull DataInput dataIn, @Nonnull DataOutput dataOut) {
		this.dataIn = checkNotNull(dataIn);
		this.dataOut = checkNotNull(dataOut);
	}

	@Override
	public void readFully(@Nonnull byte[] b) throws IOException {
		dataIn.readFully(b);
	}

	@Override
	public void readFully(@Nonnull byte[] b, int off, int len) throws IOException {
		dataIn.readFully(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return dataIn.skipBytes(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return dataIn.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return dataIn.readByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return dataIn.readUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return dataIn.readShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return dataIn.readUnsignedShort();
	}

	@Override
	public char readChar() throws IOException {
		return dataIn.readChar();
	}

	@Override
	public int readInt() throws IOException {
		return dataIn.readInt();
	}

	@Override
	public long readLong() throws IOException {
		return dataIn.readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return dataIn.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return dataIn.readDouble();
	}

	@Override
	public String readLine() throws IOException {
		return dataIn.readLine();
	}

	@Override
	@Nonnull
	public String readUTF() throws IOException {
		return dataIn.readUTF();
	}

	@Override
	public void write(int b) throws IOException {
		dataOut.write(b);
	}

	@Override
	public void write(@Nonnull byte[] b) throws IOException {
		dataOut.write(b);
	}

	@Override
	public void write(@Nonnull byte[] b, int off, int len) throws IOException {
		dataOut.write(b, off, len);
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		dataOut.writeBoolean(v);
	}

	@Override
	public void writeByte(int v) throws IOException {
		dataOut.writeByte(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		dataOut.writeShort(v);
	}

	@Override
	public void writeChar(int v) throws IOException {
		dataOut.writeChar(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		dataOut.writeInt(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		dataOut.writeLong(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		dataOut.writeFloat(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		dataOut.writeDouble(v);
	}

	@Override
	public void writeBytes(@Nonnull String s) throws IOException {
		dataOut.writeBytes(s);
	}

	@Override
	public void writeChars(@Nonnull String s) throws IOException {
		dataOut.writeChars(s);
	}

	@Override
	public void writeUTF(@Nonnull String s) throws IOException {
		dataOut.writeUTF(s);
	}
}
