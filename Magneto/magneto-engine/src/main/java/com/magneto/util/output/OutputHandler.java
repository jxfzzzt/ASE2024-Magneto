package com.magneto.util.output;



import com.magneto.dependency.MavenDependencyChain;
import com.magneto.staticanalysis.MethodCallChain;

import java.util.List;

public interface OutputHandler {
    void outputDependencyChain(List<MavenDependencyChain> dependencyChains);

    void outputMethodCallChain(List<MethodCallChain> methodCallChains);

    void outputMethodCallChain(String fileName, List<MethodCallChain> methodCallChains);

}
