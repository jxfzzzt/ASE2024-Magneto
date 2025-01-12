package com.magneto.instrument.state;

import com.magneto.util.object.CopyObjectResult;
import com.magneto.util.object.ObjectUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalStateTable {

    private static final Map<String, StateNode> STATE_TABLE = new ConcurrentHashMap<>();

    private static final Set<String> STATE_SET = new HashSet<>();


    public synchronized static void reset() {
        STATE_TABLE.clear();
        STATE_SET.clear();
    }

    public synchronized static void addMethodSignature(String methodSignature) {
        STATE_SET.add(methodSignature);
    }

    public static void addStateNode(String signature, byte value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Byte wrapperValue = value;
        StateNode stateNode = new StateNode(signature, byte.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    public static void addStateNode(String signature, boolean value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Boolean wrapperValue = value;
        StateNode stateNode = new StateNode(signature, boolean.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    public static void addStateNode(String signature, char value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Character wrapperValue = value;
        StateNode stateNode = new StateNode(signature, char.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    public static void addStateNode(String signature, short value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Short wrapperValue = value;
        StateNode stateNode = new StateNode(signature, short.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    public static void addStateNode(String signature, int value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Integer wrapperValue = value;
        StateNode stateNode = new StateNode(signature, int.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    public static void addStateNode(String signature, float value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Float wrapperValue = value;
        StateNode stateNode = new StateNode(signature, float.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    public static void addStateNode(String signature, double value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Double wrapperValue = value;
        StateNode stateNode = new StateNode(signature, double.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    public static void addStateNode(String signature, long value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        Long wrapperValue = value;
        StateNode stateNode = new StateNode(signature, long.class, wrapperValue);
        STATE_TABLE.put(signature, stateNode);
    }

    // instrument the input value of method
    public static void addStateNode(String signature, Object value) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        StateNode stateNode;
        if (value == null) {
            stateNode = new StateNode(signature, Void.class, null);
        } else {
            Class<?> clazz = value.getClass();
            Object newValue = getCopyValue(value);
            stateNode = new StateNode(signature, clazz, newValue);
        }
        STATE_TABLE.put(signature, stateNode);
    }

    // instrument the return value of method
    public static void addStateNode(Object value, String signature) {
        if (STATE_TABLE.containsKey(signature)) {
            return;
        }

        StateNode stateNode = null;
        if (value == null) {
            stateNode = new StateNode(signature, Void.class, null);
        } else {
            Class<?> clazz = value.getClass();
            Object newValue = getCopyValue(value);
            stateNode = new StateNode(signature, clazz, newValue);
        }
        STATE_TABLE.put(signature, stateNode);
    }

    private static Object getCopyValue(Object object) {
        Object newValue;
        try {
            CopyObjectResult<Object> copyResult = ObjectUtil.copyObject(object);
            object = copyResult.getInputValue();
            newValue = copyResult.getCopyValue();
        } catch (Throwable e) {
            newValue = object;
        }
        return newValue;
    }

    public synchronized static Set<String> getStateSet() {
        Set<String> stateSet = new HashSet<>();
        for (String methodSignature : STATE_SET) {
            stateSet.add(methodSignature);
        }
        return stateSet;
    }

    public synchronized static Map<String, StateNode> getStateTable() {
        Map<String, StateNode> stateTable = new HashMap<>();
        for (Map.Entry<String, StateNode> stateNodeEntry : STATE_TABLE.entrySet()) {
            StateNode value = stateNodeEntry.getValue();
            stateTable.put(stateNodeEntry.getKey(), new StateNode(value.getSignature(), value.getClazz(), value.getValue()));
        }
        return stateTable;
    }

    public synchronized static Set<String> getMethodSignatureSet() {
        return new HashSet<>(STATE_SET);
    }
}
