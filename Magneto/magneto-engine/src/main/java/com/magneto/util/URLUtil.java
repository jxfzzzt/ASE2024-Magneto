package com.magneto.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class URLUtil {
    public static URL[] stringsToUrls(String[] paths) throws MalformedURLException {
        URL[] urls = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
            urls[i] = new File(paths[i]).toURI().toURL();
        }
        return urls;
    }

    public static URL[] stringsToUrls(List<String> pathList) throws MalformedURLException {
        return stringsToUrls(pathList.toArray(new String[0]));
    }

    public static String join(String... args) {
        if (args == null || args.length == 0) {
            return "";
        }

        String url = args[0];

        for (int i = 1; i < args.length; i++) {
            if (url.endsWith("/")) {
                url += args[i];
            } else {
                url += "/" + args[i];
            }
        }
        return url;
    }
}
