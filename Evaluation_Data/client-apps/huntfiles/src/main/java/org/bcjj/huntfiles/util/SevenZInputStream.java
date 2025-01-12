package org.bcjj.huntfiles.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

public class SevenZInputStream extends InputStream {

	SevenZArchiveEntry sevenZArchiveEntry;
	SevenZFile sevenZFile;
	long size;
	
	public SevenZInputStream(SevenZArchiveEntry sevenZArchiveEntry, SevenZFile sevenZFile) {
		size=sevenZArchiveEntry.getSize();
		this.sevenZArchiveEntry=sevenZArchiveEntry;
		this.sevenZFile=sevenZFile;
	}
	
	@Override
	public int read() throws IOException {
		return sevenZFile.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return sevenZFile.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return sevenZFile.read(b, off, len);
	}
	
}