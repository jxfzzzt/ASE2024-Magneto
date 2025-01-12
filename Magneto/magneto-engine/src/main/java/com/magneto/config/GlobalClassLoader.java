package com.magneto.config;

import com.magneto.util.URLUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URLClassLoader;

@Slf4j
public class GlobalClassLoader {

    private static GlobalClassLoader globalClassLoader = null;

    private String[] jarPaths = null;

    private ClassLoader classLoader = null;

    private GlobalClassLoader(String[] jarPaths) throws MalformedURLException {
        this.jarPaths = jarPaths;
        this.classLoader = new URLClassLoader(URLUtil.stringsToUrls(jarPaths), getClass().getClassLoader());
    }

    public static void init(String[] jarPaths) throws MalformedURLException {
        if (globalClassLoader == null) {
            globalClassLoader = new GlobalClassLoader(jarPaths);
        }
    }

    public static GlobalClassLoader getInstance() {
        if (globalClassLoader != null) return globalClassLoader;
        else {
            throw new RuntimeException("global classloader is not init");
        }
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        return globalClassLoader.getClassLoader().loadClass(className);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public String[] getJarPaths() {
        return jarPaths;
    }
}
