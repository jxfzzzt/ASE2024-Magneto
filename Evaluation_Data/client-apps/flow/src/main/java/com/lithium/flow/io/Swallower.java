package com.lithium.flow.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Swallower {
	public static void close(@Nullable Closeable... closeables) {
		if (closeables != null) {
			closeAll(Arrays.asList(closeables));
		}
	}

	public static void closeAll(@Nonnull Collection<? extends Closeable> closeables) {
		for (Closeable closeable : closeables) {
			try {
				if (closeable != null) {
					closeable.close();
				}
			} catch (IOException | RuntimeException e) {
				//
			}
		}
	}
}
