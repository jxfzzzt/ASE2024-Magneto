package com.magneto.fuzz.result;

import com.magneto.staticanalysis.MethodCallChain;

import java.util.HashMap;
import java.util.Map;

public class FuzzChainResultWrapper {

    private final Map<String, FuzzChainResult> fuzzChainResultMap;

    private final MethodCallChain fuzzMethodCallChain;

    public FuzzChainResultWrapper(MethodCallChain fuzzMethodCallChain) {
        this.fuzzChainResultMap = new HashMap<>();
        this.fuzzMethodCallChain = fuzzMethodCallChain;
    }

    public void addFuzzResult(String vulName, FuzzChainResult fuzzChainResult) {
        fuzzChainResultMap.put(vulName, fuzzChainResult);
    }

    public Map<String, FuzzChainResult> getFuzzChainResult() {
        return fuzzChainResultMap;
    }

    public MethodCallChain getFuzzMethodCallChain() {
        return fuzzMethodCallChain;
    }
}
