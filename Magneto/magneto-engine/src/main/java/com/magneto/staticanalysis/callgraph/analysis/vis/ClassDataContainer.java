package com.magneto.staticanalysis.callgraph.analysis.vis;

import java.util.HashMap;
import java.util.Map;

public class ClassDataContainer {

    public Map<String, ClassData> classDataMap;

    public ClassDataContainer() {

        this.classDataMap = new HashMap<>();
    }

    public void addEntry(String className, String methodName) {
        if (!classDataMap.containsKey(className)) {
            classDataMap.put(className, new ClassData(className));
        }
        if (!classDataMap.get(className).methodDataMap.containsKey(methodName)) {
            classDataMap.get(className).methodDataMap.put(methodName, new MethodData(methodName));
        }
    }
}
