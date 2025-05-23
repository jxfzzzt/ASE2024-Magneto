package com.magneto.util.output;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
;
import com.magneto.config.GlobalConfiguration;
import com.magneto.dependency.MavenDependencyChain;
import com.magneto.staticanalysis.MethodCallChain;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TextOutputHandler implements OutputHandler {

    private static final String DEPENDENCY_CHAIN_TEXT_PATH = GlobalConfiguration.OUTPUT_DIR_PATH + File.separator + "dependency-chain.txt";

    private static final String METHOD_CALL_CHAIN_TEXT_PATH = GlobalConfiguration.OUTPUT_DIR_PATH + File.separator + "method-call-chain.txt";

    private static final File DEPENDENCY_CHAIN_TEXT_FILE = FileUtil.file(DEPENDENCY_CHAIN_TEXT_PATH);

    private static final File METHOD_CALL_CHAIN_TEXT_FILE = FileUtil.file(METHOD_CALL_CHAIN_TEXT_PATH);

    @Override
    public void outputDependencyChain(List<MavenDependencyChain> dependencyChains) {
        String output = dependencyChains.stream().map(MavenDependencyChain::toString).collect(Collectors.joining("\n"));

        FileWriter fileWriter = new FileWriter(DEPENDENCY_CHAIN_TEXT_FILE);
        fileWriter.write(output);
        log.info("the vulnerable dependency chain has been written to the {}", DEPENDENCY_CHAIN_TEXT_FILE.getAbsolutePath());

    }

    @Override
    public void outputMethodCallChain(List<MethodCallChain> methodCallChains) {
        String output = methodCallChains.stream().map(MethodCallChain::toString).collect(Collectors.joining("\n"));

        FileWriter fileWriter = new FileWriter(METHOD_CALL_CHAIN_TEXT_FILE);
        fileWriter.write(output);
        log.info("the vulnerable method call chain has been written to the {}", METHOD_CALL_CHAIN_TEXT_FILE.getAbsolutePath());
    }

    @Override
    public void outputMethodCallChain(String fileName, List<MethodCallChain> methodCallChains) {
        String output = methodCallChains.stream().map(MethodCallChain::toString).collect(Collectors.joining("\n"));

        File file = FileUtil.file(GlobalConfiguration.OUTPUT_DIR.getAbsolutePath(), fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(output);
        log.info("the vulnerable method call chain has been written to the {}", file.getAbsolutePath());
    }
}
