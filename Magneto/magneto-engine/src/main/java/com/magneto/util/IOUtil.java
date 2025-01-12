package com.magneto.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;

@Slf4j
public class IOUtil {
    public static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static String readString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public static byte[] readBytes(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return null;
        }
        InputStream in = null;
        try {
            in = Files.newInputStream(file.toPath());
            in = new BufferedInputStream(in);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            IOUtils.copy(in, bao);
            return bao.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bytesRead;
        byte[] data = new byte[4096];

        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        return buffer.toByteArray();
    }


    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(final InputStream input, final OutputStream output)
            throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(final InputStream input, final OutputStream output, final int bufferSize)
            throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
            throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(final Reader input, final Writer output) throws IOException {
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(final Reader input, final Writer output, final char[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }
}
