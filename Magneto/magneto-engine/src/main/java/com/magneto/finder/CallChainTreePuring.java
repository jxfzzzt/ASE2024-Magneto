package com.magneto.finder;

import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;

import java.util.*;
import java.util.stream.Collectors;

class CallChainTreePuring {

    private final List<MethodCallChain> methodCallChainList;

    private Map<MethodCall, Set<MethodCall>> methodCallRelations;

    private Set<MethodCall> entryMethodSet;

    private final Set<MethodCall> calleeMethodSet;

    private List<MethodCallChain> puringMethodCallChainList;

    public CallChainTreePuring(List<MethodCallChain> methodCallChainList) {
        this.methodCallChainList = methodCallChainList;
        this.calleeMethodSet = new HashSet<>();
        createMethodCallRelations();
    }

    private synchronized void createMethodCallRelations() {
        this.methodCallRelations = new HashMap<>();
        this.entryMethodSet = new HashSet<>();
        for (MethodCallChain methodCallChain : methodCallChainList) {
            List<MethodCall> methodCalls = methodCallChain.reverseChainList();
            entryMethodSet.add(methodCalls.get(0));
            for (int i = 0; i + 1 < methodCalls.size(); i++) {
                MethodCall preMethodCall = methodCalls.get(i);
                MethodCall nextMethodCall = methodCalls.get(i + 1);
                this.calleeMethodSet.add(nextMethodCall);
                Set<MethodCall> preMethodCallSet = methodCallRelations.getOrDefault(preMethodCall, new HashSet<>());
                preMethodCallSet.add(nextMethodCall);
                methodCallRelations.put(preMethodCall, preMethodCallSet);
            }
        }
    }

    public List<MethodCallChain> getPuringResult() {
        if (this.puringMethodCallChainList != null) return puringMethodCallChainList;

        this.puringMethodCallChainList = new ArrayList<>();
        for (MethodCall methodCall : entryMethodSet) {
            if (calleeMethodSet.contains(methodCall)) continue;
            depthFirstSearch(methodCall, new ArrayList<>(), new HashSet<>());
        }
        puringMethodCallChainList = puringMethodCallChainList.stream().sorted((Comparator.comparingInt(MethodCallChain::length))).collect(Collectors.toList());
        return puringMethodCallChainList;
    }

    private void depthFirstSearch(MethodCall methodCall, List<MethodCall> currentMethodCallList, Set<MethodCall> visitedMethodSet) {
        currentMethodCallList.add(methodCall);
        visitedMethodSet.add(methodCall);

        Set<MethodCall> methodCallSet = methodCallRelations.getOrDefault(methodCall, new HashSet<>());

        if (methodCallSet.isEmpty()) {
            // find the no children node
            ArrayList<MethodCall> copyMethodCalls = new ArrayList<>(currentMethodCallList);
            Collections.reverse(copyMethodCalls);
            puringMethodCallChainList.add(new MethodCallChain(copyMethodCalls));
        } else {
            for (MethodCall call : methodCallSet) {
                if (!visitedMethodSet.contains(call)) {
                    visitedMethodSet.add(call);
                    depthFirstSearch(call, currentMethodCallList, visitedMethodSet);
                }
            }
        }

        visitedMethodSet.remove(methodCall);
        currentMethodCallList.remove(currentMethodCallList.size() - 1);
    }
}
