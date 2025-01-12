package com.magneto.staticanalysis.callgraph.analysis.vis;


import java.util.HashMap;
import java.util.Map;

public class ClassData {

    public String className;


    public Map<String, MethodData> methodDataMap;


    public ClassData(String className) {
        this.className = className;
        this.methodDataMap = new HashMap<>();
    }


}
