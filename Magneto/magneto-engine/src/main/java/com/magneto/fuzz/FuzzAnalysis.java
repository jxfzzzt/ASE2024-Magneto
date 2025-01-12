package com.magneto.fuzz;

import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.taint.TaintField;
import com.magneto.staticanalysis.taint.TaintObject;
import com.magneto.staticanalysis.taint.TaintParam;

import java.util.Map;
import java.util.Set;

public class FuzzAnalysis {

    private final Set<TaintObject> controlFlowTaintObjects;

    private final Set<TaintObject> methodParamTaintObjects;

    private final Map<Integer, Set<TaintObject>> methodParamMap;

    private final Set<TaintObject> variableTaintObjects;

    public FuzzAnalysis(Set<TaintObject> controlFlowTaintObjects,
                        Set<TaintObject> methodParamTaintObjects,
                        Map<Integer, Set<TaintObject>> methodParamMap,
                        Set<TaintObject> variableTaintObjects) {
        this.controlFlowTaintObjects = controlFlowTaintObjects;
        this.methodParamTaintObjects = methodParamTaintObjects;
        this.methodParamMap = methodParamMap;
        this.variableTaintObjects = variableTaintObjects;
    }

    public boolean preCheck(MethodCall targetMethodCall, MethodCall triggeredMethodCall) {
        if (triggeredMethodCall.getArgNum() > 0 && methodParamTaintObjects.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean cfgContains(String fieldSignature) {
        if (controlFlowTaintObjects == null) return false;

        for (TaintObject controlFlowTaintObject : controlFlowTaintObjects) {
            if (controlFlowTaintObject instanceof TaintField) {
                String signature = ((TaintField) controlFlowTaintObject).getFieldSignature();
                if (signature.equals(fieldSignature)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cfgContains(String methodSignature, int index) {
        if (controlFlowTaintObjects == null) return false;

        for (TaintObject controlFlowTaintObject : controlFlowTaintObjects) {
            if (controlFlowTaintObject instanceof TaintParam) {
                TaintParam taintParam = (TaintParam) controlFlowTaintObject;
                if (taintParam.getMethodSignature().equals(methodSignature) && taintParam.getIndex() == index) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean methodParamContains(String fieldSignature) {
        if (methodParamTaintObjects == null) return false;

        for (TaintObject methodParamTaintObject : methodParamTaintObjects) {
            if (methodParamTaintObject instanceof TaintField) {
                String signature = ((TaintField) methodParamTaintObject).getFieldSignature();
                if (signature.equals(fieldSignature)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean methodParamContains(String methodSignature, int index) {
        if (methodParamTaintObjects == null) return false;

        for (TaintObject methodParamTaintObject : methodParamTaintObjects) {
            if (methodParamTaintObject instanceof TaintParam) {
                TaintParam taintParam = (TaintParam) methodParamTaintObject;
                if (taintParam.getMethodSignature().equals(methodSignature) && taintParam.getIndex() == index) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVariableSource(String fieldSignature) {
        if (variableTaintObjects == null) return false;

        for (TaintObject variableTaintObject : variableTaintObjects) {
            if (variableTaintObject instanceof TaintField) {
                String signature = ((TaintField) variableTaintObject).getFieldSignature();
                if (signature.equals(fieldSignature)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVariableSource(String methodSignature, int index) {
        if (variableTaintObjects == null) return false;

        for (TaintObject variableTaintObject : variableTaintObjects) {
            if (variableTaintObject instanceof TaintParam) {
                TaintParam taintParam = (TaintParam) variableTaintObject;
                if (taintParam.getMethodSignature().equals(methodSignature) && taintParam.getIndex() == index) {
                    return true;
                }
            }
        }
        return false;
    }

    public Integer locateParamPos(String fieldSignature) {
        if (methodParamMap == null) return -1;

        for (Map.Entry<Integer, Set<TaintObject>> entry : methodParamMap.entrySet()) {
            Set<TaintObject> taintObjectSet = entry.getValue();
            for (TaintObject taintObject : taintObjectSet) {
                if (taintObject instanceof TaintField) {
                    TaintField taintField = (TaintField) taintObject;
                    if (taintField.getFieldSignature().equals(fieldSignature)) {
                        return entry.getKey();
                    }
                }
            }
        }
        return -1;
    }

    public Integer locateParamPos(String methodSignature, int index) {
        if (methodParamMap == null) return -1;

        for (Map.Entry<Integer, Set<TaintObject>> entry : methodParamMap.entrySet()) {
            Set<TaintObject> taintObjectSet = entry.getValue();
            for (TaintObject taintObject : taintObjectSet) {
                if (taintObject instanceof TaintParam) {
                    TaintParam taintParam = (TaintParam) taintObject;
                    if (taintParam.getIndex() == index && taintParam.getMethodSignature().equals(methodSignature)) {
                        return entry.getKey();
                    }
                }
            }
        }
        return -1;
    }

    public TaintField getTaintFieldFromMethodParams(String fieldSignature) {
        if (methodParamTaintObjects == null) return null;

        for (TaintObject methodParamTaintObject : methodParamTaintObjects) {
            if (methodParamTaintObject instanceof TaintField) {
                String signature = ((TaintField) methodParamTaintObject).getFieldSignature();
                if (signature.equals(fieldSignature)) {
                    return (TaintField) methodParamTaintObject;
                }
            }
        }

        return null;
    }

    public TaintParam getTaintParamFromMethodParams(String methodSignature, int index) {
        if (methodParamTaintObjects == null) return null;

        for (TaintObject methodParamTaintObject : methodParamTaintObjects) {
            if (methodParamTaintObject instanceof TaintParam) {
                TaintParam taintParam = (TaintParam) methodParamTaintObject;
                if (taintParam.getMethodSignature().equals(methodSignature) && taintParam.getIndex() == index) {
                    return taintParam;
                }
            }
        }
        return null;
    }
}
