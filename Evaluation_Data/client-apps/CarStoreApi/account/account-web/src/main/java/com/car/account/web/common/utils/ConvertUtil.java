package com.car.account.web.common.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author linjiang.xie
 * @date 2019/6/11 15:29
 */
public class ConvertUtil {
    /**
     * inputStream转outputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parse(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

    /**
     * outputStream转inputStream
     * @param out
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parse(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    /**
     * inputStream转String
     * @param in
     * @return
     * @throws Exception
     */
    public static String parse_String(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream.toString();
    }

    /**
     * OutputStream 转String
     * @param out
     * @return
     * @throws Exception
     */
    public static String parse_String(final OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }

    /**
     * String转inputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream parse_inputStream(final String in) throws Exception {
        final ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
        return input;
    }

    /**
     * String 转outputStream
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parse_outputStream(final String in) throws Exception {
        return parse(parse_inputStream(in));
    }
}
