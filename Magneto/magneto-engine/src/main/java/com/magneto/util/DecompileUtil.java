package com.magneto.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.github.kwart.jd.JavaDecompiler;
import com.github.kwart.jd.input.JDInput;
import com.github.kwart.jd.input.ZipFileInput;
import com.github.kwart.jd.options.DecompilerOptions;
import com.github.kwart.jd.output.DirOutput;
import com.github.kwart.jd.output.JDOutput;
import com.magneto.asm.ClassInfo;
import com.magneto.config.GlobalConfiguration;
import com.magneto.dependency.MavenDependency;
import lombok.extern.slf4j.Slf4j;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.util.getopt.Options;
import org.benf.cfr.reader.util.getopt.OptionsImpl;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DecompileUtil {

    public static File decompileDependency(MavenDependency dependency) {
        String descriptor = dependency.getDescriptor();
        File outputFile = FileUtil.file(GlobalConfiguration.DECOMPILE_DIR.getAbsolutePath(), descriptor);
        JDInput input = new ZipFileInput(dependency.getJarFile().getAbsolutePath());
        JDOutput output = new DirOutput(outputFile);
        JavaDecompiler decompiler = new JavaDecompiler(new DecompilerOptions() {
            @Override
            public boolean isSkipResources() {
                return false;
            }

            @Override
            public boolean isEscapeUnicodeCharacters() {
                return false;
            }

            @Override
            public boolean isDisplayLineNumbers() {
                return false;
            }

            @Override
            public boolean isParallelProcessingAllowed() {
                return true;
            }
        });
        input.decompile(decompiler, output);
        return outputFile;
    }

    public static File decompileJarFile(String jarFilePath, String outputFile) {
        JDInput input = new ZipFileInput(jarFilePath);
        File file = new File(outputFile);
        JDOutput output = new DirOutput(file);
        JavaDecompiler decompiler = new JavaDecompiler(new DecompilerOptions() {
            @Override
            public boolean isSkipResources() {
                return false;
            }

            @Override
            public boolean isEscapeUnicodeCharacters() {
                return false;
            }

            @Override
            public boolean isDisplayLineNumbers() {
                return false;
            }

            @Override
            public boolean isParallelProcessingAllowed() {
                return true;
            }
        });
        input.decompile(decompiler, output);
        return file;
    }

    @Deprecated
    public static String decompileBytecode(String classFilePath) {
        byte[] bytes = IOUtil.readBytes(classFilePath);
        ClassInfo classInfo = ASMUtil.getClassInfo(bytes);
        String name = classInfo.getName();
        String destName = name.replaceAll("\\.", "/") + ".java";

        CfrDriver cfrDriver = new CfrDriver.Builder().withBuiltOptions(getOptions(null)).build();
        cfrDriver.analyse(Collections.singletonList(classFilePath));
        File destFile = FileUtil.file(GlobalConfiguration.DECOMPILE_PATH, destName);
        if (destFile.isFile()) {
            FileReader reader = new FileReader(destFile, Charset.defaultCharset());
            return reader.readString();
        } else {
            return null;
        }
    }

    @Deprecated
    public static String decompileBytecode(byte[] bytecode) {
        ClassInfo classInfo = ASMUtil.getClassInfo(bytecode);
        String name = classInfo.getName();
        String destName = name.replaceAll("\\.", "/") + ".java";

        File file = FileUtil.file(GlobalConfiguration.DECOMPILE_DIR.getAbsolutePath(), "temp.class");
        FileWriter writer = new FileWriter(file);
        writer.write(bytecode, 0, bytecode.length);

        CfrDriver cfrDriver = new CfrDriver.Builder().withBuiltOptions(getOptions(null)).build();
        cfrDriver.analyse(Collections.singletonList(file.getAbsolutePath()));
        File destFile = FileUtil.file(GlobalConfiguration.DECOMPILE_PATH, destName);
        if (destFile.isFile()) {
            FileReader reader = new FileReader(destFile, Charset.defaultCharset());
            return reader.readString();
        } else {
            return null;
        }
    }

    @Deprecated
    public static String decompileMethod(String classFilePath, String methodName) {
        byte[] bytes = IOUtil.readBytes(classFilePath);
        ClassInfo classInfo = ASMUtil.getClassInfo(bytes);
        String name = classInfo.getName();
        String destName = name.replaceAll("\\.", "/") + ".java";

        CfrDriver cfrDriver = new CfrDriver.Builder().withBuiltOptions(getOptions(methodName)).build();
        cfrDriver.analyse(Collections.singletonList(classFilePath));
        File destFile = FileUtil.file(GlobalConfiguration.DECOMPILE_PATH, destName);
        if (destFile.isFile()) {
            FileReader reader = new FileReader(destFile, Charset.defaultCharset());
            return reader.readString();
        } else {
            return null;
        }
    }

    @Deprecated
    public static String decompileMethod(byte[] bytecode, String methodName) {
        ClassInfo classInfo = ASMUtil.getClassInfo(bytecode);
        String name = classInfo.getName();
        String destName = name.replaceAll("\\.", "/") + ".java";

        File file = FileUtil.file(GlobalConfiguration.DECOMPILE_DIR.getAbsolutePath(), "temp.class");
        FileWriter writer = new FileWriter(file);
        writer.write(bytecode, 0, bytecode.length);

        CfrDriver cfrDriver = new CfrDriver.Builder().withBuiltOptions(getOptions(methodName)).build();
        cfrDriver.analyse(Collections.singletonList(file.getAbsolutePath()));
        File destFile = FileUtil.file(GlobalConfiguration.DECOMPILE_PATH, destName);
        if (destFile.isFile()) {
            FileReader reader = new FileReader(destFile, Charset.defaultCharset());
            return reader.readString();
        } else {
            return null;
        }
    }

    // options about outputdir and decompiled method name
    private static Options getOptions(String methodName) {
        Map<String, String> optMap = new HashMap<>();
        optMap.put("outputdir", GlobalConfiguration.DECOMPILE_PATH); // CFR require options
        if (methodName != null) {
            optMap.put("methodname", methodName);
        }
        return new OptionsImpl(optMap);
    }

}
