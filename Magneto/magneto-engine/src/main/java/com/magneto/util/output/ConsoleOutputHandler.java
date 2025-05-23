package com.magneto.util.output;


import com.magneto.dependency.MavenDependency;
import com.magneto.dependency.MavenDependencyChain;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ConsoleOutputHandler implements OutputHandler {
    @Override
    public void outputDependencyChain(List<MavenDependencyChain> dependencyChains) {
        log.info("the result of vulnerable dependency chain: ");
        for (int i = 0; i < dependencyChains.size(); i++) {
            log.info("the {}th vulnerable dependency chain: ", i + 1);
            List<MavenDependency> mavenDependencies = dependencyChains.get(i).forwardChainList();
            for (int j = 0; j < mavenDependencies.size(); j++) {
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < j; k++) sb.append("   ");
                sb.append(mavenDependencies.get(j).toString());
                log.info(sb.toString());
            }
        }
    }

    @Override
    public void outputMethodCallChain(List<MethodCallChain> methodCallChains) {
        log.info("the result of vulnerable method call chain: ");
        for (int i = 0; i < methodCallChains.size(); i++) {
            log.info("the {}th vulnerable method call chain: ", i + 1);
            List<MethodCall> methodCalls = methodCallChains.get(i).forwardChainList();
            for (int j = 0; j < methodCalls.size(); j++) {
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < j; k++) sb.append("   ");
                sb.append(methodCalls.get(j).toString());
                log.info(sb.toString());
            }
        }
    }

    @Override
    public void outputMethodCallChain(String fileName, List<MethodCallChain> methodCallChains) {
        throw new UnsupportedOperationException("not support output to a file");
    }
}
