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

package com.lithium.flow.shell.sshj;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.io.DataIo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.annotation.Nonnull;

import net.schmizz.sshj.sftp.RemoteFile;

/**
 * @author Matt Ayres
 */
public class SshjDataIo implements DataIo {
	private final RemoteFile file;
	private long fp;

	private final byte[] b1 = new byte[1];
	private final byte[] b2 = new byte[2];
	private final byte[] b4 = new byte[4];
	private final byte[] b8 = new byte[8];

	public SshjDataIo(@Nonnull RemoteFile file) {
		this.file = checkNotNull(file);
	}

	@Override
	public long getFilePointer() {
		return fp;
	}

	@Override
	public void seek(long pos) {
		fp = pos;
	}

	@Override
	public long length() throws IOException {
		return file.length();
	}

	@Override
	public void close() throws IOException {
		file.close();
	}

	@Override
	public void readFully(@Nonnull byte[] b) throws IOException {
		readFully(b, 0, b.length);
	}

	@Override
	public void readFully(@Nonnull byte[] b, int off, int len) throws IOException {
		int n = 0;
		while (n < len) {
			n += readFile(b, off + n, len - n);
		}
	}

	private void readFile(@Nonnull byte[] b) throws IOException {
		readFile(b, 0, b.length);
	}

	private int readFile(@Nonnull byte[] b, int off, int len) throws IOException {
		int count = file.read(fp, b, off, len);
		if (count < 0) {
			throw new EOFException();
		}
		fp += count;
		return count;
	}

	@Override
	public int skipBytes(int n) throws IOException {
		long last = fp;
		fp = Math.min(fp + Math.max(n, 0), file.length());
		return (int) (last - fp);
	}

	@Override
	public boolean readBoolean() throws IOException {
		readFile(b1);
		return b1[0] != 0;
	}

	@Override
	public byte readByte() throws IOException {
		return (byte) readUnsignedByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		readFile(b1);
		return b1[0] & 0xFF;
	}

	@Override
	public short readShort() throws IOException {
		return (short) readUnsignedShort();
	}


	@Override
	public int readUnsignedShort() throws IOException {
		readFile(b2);
		return b2[0] << 8 & 0xFF00 | b2[1] & 0xFF;
	}

	@Override
	public char readChar() throws IOException {
		readFile(b2);
		return (char) (b2[0] << 8 & 0xFF00 | b2[1] & 0xFF);
	}

	@Override
	public int readInt() throws IOException {
		readFile(b4);
		return b4[0] << 24 & 0xFF000000 | b4[1] << 16 & 0xFF0000 | b4[2] << 8 & 0xFF00 | b4[3] & 0xFF;
	}

	@Override
	public long readLong() throws IOException {
		readFile(b8);
		return (long) (b8[0] << 24 & 0xFF000000 | b8[1] << 16 & 0xFF0000 | b8[2] << 8 & 0xFF00 | b8[3] & 0xFF) << 32
				| (b8[4] << 24 & 0xFF000000L | b8[5] << 16 & 0xFF0000L | b8[6] << 8 & 0xFF00L | b8[7] & 0xFFL);
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public String readLine() throws IOException {
		StringBuilder input = new StringBuilder();
		int c = -1;
		boolean eol = false;

		while (!eol)
			switch (c = readUnsignedByte()) {
				case -1:
				case '\n':
					eol = true;
					break;
				case '\r':
					eol = true;
					long cur = getFilePointer();
					if (readUnsignedByte() != '\n') {
						seek(cur);
					}
					break;
				default:
					input.append((char) c);
					break;
			}

		if (c == -1 && input.length() == 0) {
			return null;
		}
		return input.toString();
	}

	@Override
	@Nonnull
	public String readUTF() throws IOException {
		return DataInputStream.readUTF(this);
	}

	@Override
	public void write(int b) throws IOException {
		b1[0] = (byte) b;
		write(b1);
	}

	@Override
	public void write(@Nonnull byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	@Override
	public void write(@Nonnull byte[] b, int off, int len) throws IOException {
		if (off == 0) {
			file.write(fp, b, off, len);
		} else {
			// workaround for bug in RemoteFile where 'len - off' is sent instead of 'len'
			byte[] c = new byte[len];
			System.arraycopy(b, off, c, 0, len);
			file.write(fp, c, 0, len);
		}
		fp += len;
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		write(v ? 1 : 0);
	}

	@Override
	public void writeByte(int v) throws IOException {
		write(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		b2[0] = (byte) (v >>> 8);
		b2[1] = (byte) v;
		write(b2, 0, 2);
	}

	@Override
	public void writeChar(int v) throws IOException {
		writeShort(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		b4[0] = (byte) (v >>> 24);
		b4[1] = (byte) (v >>> 16);
		b4[2] = (byte) (v >>> 8);
		b4[3] = (byte) v;
		write(b4, 0, 4);
	}

	@Override
	public void writeLong(long v) throws IOException {
		b8[0] = (byte) (v >>> 56);
		b8[1] = (byte) (v >>> 48);
		b8[2] = (byte) (v >>> 40);
		b8[3] = (byte) (v >>> 32);
		b8[4] = (byte) (v >>> 24);
		b8[5] = (byte) (v >>> 16);
		b8[6] = (byte) (v >>> 8);
		b8[7] = (byte) v;
		write(b8, 0, 8);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		writeInt(Float.floatToIntBits(v));
	}

	@Override
	public void writeDouble(double v) throws IOException {
		writeLong(Double.doubleToLongBits(v));
	}

	@Override
	public void writeBytes(@Nonnull String s) throws IOException {
		write(s.getBytes());
	}

	@Override
	public void writeChars(@Nonnull String s) throws IOException {
		for (char c : s.toCharArray()) {
			writeChar(c);
		}
	}

	@Override
	public void writeUTF(@Nonnull String s) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new DataOutputStream(baos).writeUTF(s);
		write(baos.toByteArray());
	}
}
