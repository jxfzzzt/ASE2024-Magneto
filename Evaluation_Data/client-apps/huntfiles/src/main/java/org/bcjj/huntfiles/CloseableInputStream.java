package org.bcjj.huntfiles;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class CloseableInputStream implements Closeable {

	
	/**
	 * Para obtener el inputStream hay que crear primero un new CloseableInputStream desde este PreCloseableInputStream
	 * De esta manera queda claro que se debe hacer el close del flujo retornado.
	 */
	public static class PreCloseableInputStream {
		private InputStream inputStream;
		public PreCloseableInputStream(InputStream inputStream) {
			this.inputStream=inputStream;
		}
		private InputStream getInputStream() {
			return inputStream;
		}
	}	
	
	
	
	private InputStream inputStream;
	public CloseableInputStream(PreCloseableInputStream preCloseableInputStream) {
		this.inputStream=preCloseableInputStream.getInputStream();
	}
	
	@Override
	public void close() throws IOException {
		inputStream.close();
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	
}
