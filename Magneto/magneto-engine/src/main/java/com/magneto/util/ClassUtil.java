package com.magneto.util;

import cn.hutool.core.io.FileUtil;
import com.magneto.config.ClientProjectProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

@Slf4j
public class ClassUtil {

    // jarPath --> List<Class>
    public static List<Class> getClassesFromJar(String jarPath) throws IOException {
        List<Class> classList = new ArrayList<>();
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(FileUtil.file(jarPath));
        } catch (ZipException e) {
            return classList;
        }

        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = entry.getName().replace("/", ".").replace(".class", "");

                Class<?> clazz = null;
                try {
                    clazz = ClientProjectProperty.getInstrumentedFuzzLoader().loadClass(className);
                } catch (Throwable e) {
//                    log.warn("'{}'" + " load warning ", className);
                }

                if (clazz == null) continue;
                classList.add(clazz);
            }
        }
        jarFile.close();

        return classList;
    }

    public static byte[] getClassByte(String jarPath, String className) throws MalformedURLException, ClassNotFoundException {
        String classPrefix = className.substring(0, className.lastIndexOf('.') - 1);
        classPrefix = classPrefix.replaceAll("\\.", "/");
        String classResourceName = classPrefix + className.substring(className.lastIndexOf('.'));

        try (
                JarFile jarFile = new JarFile(jarPath);
                InputStream in = jarFile.getInputStream(jarFile.getJarEntry(classResourceName))
        ) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            IOUtil.copy(in, bao);
            return bao.toByteArray();
        } catch (IOException e) {
            log.error("ReadClassByteError: ", e);
            return null;
        }
    }

    public static Boolean isArray(String className) {
        if (className == null) return false;
        return className.startsWith("[") || className.contains("[]");
    }

    public static byte[] getClassBytes(ClassLoader loader, String className) throws IOException {
        String internalName = className.replace('.', '/') + ".class";
        InputStream inputStream = loader.getResourceAsStream(internalName);
        if (inputStream != null) {
            return IOUtil.readBytes(inputStream);
        } else {
            return null;
        }
    }
}
