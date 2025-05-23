package com.magneto.util.output;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import com.magneto.config.GlobalConfiguration;
import com.magneto.dependency.MavenDependency;
import com.magneto.dependency.MavenDependencyChain;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;
import com.magneto.testcase.model.TestcaseUnit;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonOutputHandler implements OutputHandler {

    @Override
    public void outputDependencyChain(List<MavenDependencyChain> dependencyChains) {
        JSONArray jsonArray = new JSONArray();
        for (MavenDependencyChain dependencyChain : dependencyChains) {
            List<MavenDependency> mavenDependencies = dependencyChain.forwardChainList();
            JSONArray array = new JSONArray();
            for (MavenDependency mavenDependency : mavenDependencies) {
                array.add(mavenDependency.toString());
            }
            jsonArray.add(array);
        }

        String DEPENDENCY_CHAIN_JSON_PATH = GlobalConfiguration.OUTPUT_DIR_PATH + File.separator + "dependency-chain.json";
        File DEPENDENCY_CHAIN_JSON_FILE = FileUtil.file(DEPENDENCY_CHAIN_JSON_PATH);
        FileWriter fileWriter = new FileWriter(DEPENDENCY_CHAIN_JSON_FILE);
        fileWriter.write(jsonArray.toStringPretty());
        log.info("the vulnerable dependency chain has been written to the {}", DEPENDENCY_CHAIN_JSON_FILE.getAbsolutePath());
    }

    @Override
    public void outputMethodCallChain(List<MethodCallChain> methodCallChains) {
        JSONArray jsonArray = new JSONArray();
        for (MethodCallChain methodCallChain : methodCallChains) {
            List<MethodCall> methodCalls = methodCallChain.forwardChainList();
            JSONArray array = new JSONArray();
            for (MethodCall methodCall : methodCalls) {
                array.add(methodCall.toString());
            }
            jsonArray.add(array);
        }

        String METHOD_CALL_CHAIN_JSON_PATH = GlobalConfiguration.OUTPUT_DIR_PATH + File.separator + "method-call-chain.json";
        File METHOD_CALL_CHAIN_JSON_FILE = FileUtil.file(METHOD_CALL_CHAIN_JSON_PATH);

        FileWriter fileWriter = new FileWriter(METHOD_CALL_CHAIN_JSON_FILE);
        fileWriter.write(jsonArray.toStringPretty());
        log.info("the vulnerable method call chain has been written to the {}", METHOD_CALL_CHAIN_JSON_FILE.getAbsolutePath());
    }

    @Override
    public void outputMethodCallChain(String fileName, List<MethodCallChain> methodCallChains) {
        JSONArray jsonArray = new JSONArray();
        for (MethodCallChain methodCallChain : methodCallChains) {
            List<MethodCall> methodCalls = methodCallChain.forwardChainList();

            JSONArray chainArray = new JSONArray();
            for (MethodCall methodCall : methodCalls) {
                chainArray.add(methodCall.toString());
            }

            JSONArray vulArray = new JSONArray();
            for (Map.Entry<String, List<TestcaseUnit>> entry : methodCallChain.getTestcaseMap().entrySet()) {
                vulArray.add(entry.getKey());
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("chain", chainArray);
            jsonObject.put("vuls", vulArray);
            jsonArray.add(jsonObject);
        }

        File file = FileUtil.file(GlobalConfiguration.OUTPUT_DIR.getAbsolutePath(), fileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonArray.toStringPretty());
        log.info("the vulnerable method call chain has been written to the {}", file.getAbsolutePath());
    }
}
