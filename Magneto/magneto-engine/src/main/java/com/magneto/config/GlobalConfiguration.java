package com.magneto.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;

import java.io.File;

public class GlobalConfiguration {

    private static final ConfigProperty CONFIG_PROPERTY = SpringUtil.getBean(ConfigProperty.class);

    public static final String CONFIGURATION_FILE_NAME = "application.properties";

    public static String TARGET_VULNERABILITY = null;

    public static final String MAVEN_EXEC_COMMAND = "mvn clean package -DskipTests";

    public static final String WORK_DIR_PATH = System.getProperty("user.dir");

    public static final String CACHE_DIR_PATH = System.getProperty("user.dir") + File.separator + ".dummy";

    public static String OUTPUT_DIR_PATH = System.getProperty("user.dir") + File.separator + CONFIG_PROPERTY.getOutputDir();

    public static final String JAR_DIR_PATH = CACHE_DIR_PATH + File.separator + "jar-files";

    public static final String DECOMPILE_PATH = CACHE_DIR_PATH + File.separator + "decompile";

    public static final File WORK_DIR = FileUtil.file(WORK_DIR_PATH);

    public static final File CACHE_DIR = FileUtil.file(CACHE_DIR_PATH);

    public static File OUTPUT_DIR = FileUtil.file(OUTPUT_DIR_PATH);

    public static final File DECOMPILE_DIR = FileUtil.file(DECOMPILE_PATH);

}
