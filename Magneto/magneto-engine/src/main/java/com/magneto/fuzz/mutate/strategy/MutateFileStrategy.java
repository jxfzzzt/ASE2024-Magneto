package com.magneto.fuzz.mutate.strategy;

import cn.hutool.core.io.FileUtil;
import com.magneto.fuzz.mutate.MutationStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class MutateFileStrategy implements MutationStrategy<File> {
    @Override
    public File mutate(Class clazz, File obj) {
        if (Math.random() < SET_NULL_VALUE) {
            return null;
        } else {
            File file = (File) obj;
            if (file == null) {
                return FileUtil.createTempFile();
            } else {
                if (Math.random() < 0.75 && file.exists()) {
                    return file.getParentFile();
                } else {
                    return FileUtil.createTempFile();
                }
            }
        }
    }
}
