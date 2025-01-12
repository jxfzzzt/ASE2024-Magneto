package com.magneto.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.magneto.config.GlobalConfiguration;
import com.magneto.dependency.MavenDependency;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JarUtil {

    private static final String DEFAULT_JAR_NAME_SCHEMA = "{}-{}.jar";

    private static final String CACHE_JAR_NAME_SCHEMA = "{}__fdse__{}__fdse__{}.jar";

    public static ClassLoader loadClassFromJar(String... jarPaths) throws MalformedURLException {
        final Path[] jarPathArr = new Path[jarPaths.length];
        // get the path of the Jar package
        for (int i = 0; i < jarPaths.length; i++) {
            Path path = Paths.get(jarPaths[i]).toAbsolutePath();
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("invalid jar path: " + path);
            }
            jarPathArr[i] = path;
        }
        return loadClassFromJar(jarPathArr);
    }

    public static ClassLoader loadClassFromJar(Path... jarPaths) throws MalformedURLException {
        final List<URL> classPathUrls = new ArrayList<>(jarPaths.length);
        for (Path jarPath : jarPaths) {
            if (jarPath == null || !Files.exists(jarPath) || Files.isDirectory(jarPath)) {
                throw new IllegalArgumentException("path \"" + jarPath + "\" is not a path to a file.");
            }
            classPathUrls.add(jarPath.toUri().toURL());
        }
        return new URLClassLoader(classPathUrls.toArray(new URL[0]), JarUtil.class.getClassLoader());
    }

    public static File downloadJar(String groupId, String artifactId, String version, File file, List<String> repoUrls) {
        if (file.exists()) return file;

        groupId = groupId.replaceAll("\\.", "/");
        String fileName = StrUtil.format(DEFAULT_JAR_NAME_SCHEMA, artifactId, version);

        for (String baseUrl : repoUrls) {
            try {
                String downloadUrl = URLUtil.join(baseUrl, groupId, artifactId, version, fileName);
                HttpUtil.downloadFile(downloadUrl, file);
                break;
            } catch (HttpException ignored) {
            }
        }

        return file;
    }

    public static File downloadJar(String groupId, String artifactId, String version, List<String> repoUrls) {
        // default jar file name
        String cacheJarName = StrUtil.format(CACHE_JAR_NAME_SCHEMA, groupId, artifactId, version);
        File downJarFile = FileUtil.file(GlobalConfiguration.JAR_DIR_PATH, cacheJarName);
        // TODO speeding
        return downJarFile;
//        return downloadJar(groupId, artifactId, version, downJarFile, repoUrls);
    }

    public static File downloadJar(MavenDependency mavenDependency, List<String> repoUrls) {
        String groupId = mavenDependency.getGroupId();
        String artifactId = mavenDependency.getArtifactId();
        String version = mavenDependency.getVersion();
        return downloadJar(groupId, artifactId, version, repoUrls);
    }

}
