package com.magneto.finder;

import cn.hutool.extra.spring.SpringUtil;
import com.magneto.config.ClientProjectProperty;
import com.magneto.config.ConfigProperty;
import com.magneto.dependency.MavenDependency;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;
import com.magneto.staticanalysis.callgraph.CallGraphBean;
import com.magneto.staticanalysis.callgraph.ProjectCallGraph;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import com.magneto.testcase.TestCaseService;
import com.magneto.testcase.model.TestcaseUnit;
import lombok.extern.slf4j.Slf4j;
import soot.SootMethod;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class VulCallChainFinder {
    private static final Integer MAX_SEARCH_THREAD = 15;

    private static final TestCaseService TEST_CASE_SERVICE = SpringUtil.getBean(TestCaseService.class);

    private final ProjectCallGraph projectCallGraph;

    private List<MethodCallChain> vulCallChainList;

    private final List<MethodCallChain> puringVulCallChainList;

    private final VulDependencyFinder vulDependencyFinder;

    private final Set<String> vulMethodSignatureSet;

    private final Map<String, Map<String, List<TestcaseUnit>>> methodSigTestcaseUnitMap; // method signature ---> Map<cve name, testcase unit list>

    private final Integer maxCallChainLength;

    // find vulnerable call method chain according to the vulnerable dependency chain
    public VulCallChainFinder(ProjectCallGraph projectCallGraph, VulDependencyFinder vulDependencyFinder) {
        this.projectCallGraph = projectCallGraph;
        this.vulDependencyFinder = vulDependencyFinder;
        this.vulCallChainList = Collections.synchronizedList(new ArrayList<MethodCallChain>());
        this.vulMethodSignatureSet = new HashSet<>();
        this.methodSigTestcaseUnitMap = new HashMap<>();

        this.maxCallChainLength = SpringUtil.getBean(ConfigProperty.class).getMaxCallChainLength();
        loadVulMethod();
        findVulCallChain();
        trimMethodCallChains();

        puringVulCallChainList = new CallChainTreePuring(vulCallChainList).getPuringResult();
        for (MethodCallChain methodCallChain : puringVulCallChainList) {
            String vulSignature = methodCallChain.getVulMethodCall().getMethodSignature();
            methodCallChain.setTestcaseMap(methodSigTestcaseUnitMap.get(vulSignature));
        }
    }

    private void loadVulMethod() {
        // find vulnerable method in dependency
        Set<MavenDependency> vulDependencySet = vulDependencyFinder.getVulDependencySet();

        for (MavenDependency dependency : vulDependencySet) {
            List<TestcaseUnit> testcaseUnitList = TEST_CASE_SERVICE.getTestcaseUnitsByDependency(dependency);
            for (TestcaseUnit testcaseUnit : testcaseUnitList) {
                String vulMethodSignature = testcaseUnit.getVulMethodSignature();
                vulMethodSignatureSet.add(vulMethodSignature);

                Map<String, List<TestcaseUnit>> map = methodSigTestcaseUnitMap.getOrDefault(vulMethodSignature, new HashMap<>());
                String vulName = testcaseUnit.getVulName();
                List<TestcaseUnit> testcaseUnits = map.getOrDefault(vulName, new ArrayList<>());
                testcaseUnits.add(testcaseUnit);
                map.put(vulName, testcaseUnits);
                methodSigTestcaseUnitMap.put(vulMethodSignature, map);
            }
        }
    }

    private void findVulCallChain() {
        CallGraphBean callGraphBean = projectCallGraph.getCallGraphBean();
        List<Node> rootNodes = callGraphBean.getCiaMethod();
        List<Node> visitedNodes = Collections.synchronizedList(new ArrayList<>());
        for (Node node : rootNodes) {
            if (node != null && node.getMethod() != null) {
                visitedNodes.add(node);
            }
        }

        // multi-thread execute
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_SEARCH_THREAD);
        // init CountDownLatch
        CountDownLatch latch = new CountDownLatch(visitedNodes.size());
        for (Node visitedNode : visitedNodes) {
            threadPool.submit(() -> {
                try {
                    depthSearchFindCallChain(visitedNode,
                            Collections.synchronizedList(new ArrayList<>()),
                            Collections.synchronizedSet(new HashSet<>()), 0);
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }

        threadPool.shutdown();
    }

    // DFS search method call chain
    private void depthSearchFindCallChain(Node node, List<MethodCall> currentCallList, Set<String> visitMethodSignatureSet, int depth) {

        if (node == null || depth >= maxCallChainLength) {
            return;
        }

        MethodCall methodCall = wrapMethod(node.getMethod());
        if (methodCall == null) {
            return;
        }

        currentCallList.add(methodCall);
        visitMethodSignatureSet.add(methodCall.getMethodSignature());

        if (currentCallList.size() <= this.maxCallChainLength) {
            // check vulnerable method call
            if (isVulMethod(methodCall)) {
                MethodCallChain methodCallChain = new MethodCallChain(new ArrayList<>(currentCallList));
                methodCallChain.setTestcaseMap(methodSigTestcaseUnitMap.get(methodCall.getMethodSignature()));
                vulCallChainList.add(methodCallChain);
            } else {
                if (node.getChildren() != null) {
                    for (AbstractNode child : node.getChildren()) {
                        if (child == null) continue;
                        if (!visitMethodSignatureSet.contains(((Node) child).getMethod().getSignature())) {
                            visitMethodSignatureSet.add(((Node) child).getMethod().getSignature());
                            depthSearchFindCallChain((Node) child, currentCallList, visitMethodSignatureSet, depth + 1);
                        }
                    }
                }
            }
        }

        // remove the last one method call
        currentCallList.remove(currentCallList.size() - 1);
        visitMethodSignatureSet.remove(methodCall.getMethodSignature());
    }

    // trim call chain list
    private void trimMethodCallChains() {
        List<MethodCallChain> trimCallChainList = new ArrayList<>();
        for (MethodCallChain callChain : this.vulCallChainList) {
            MethodCall firstCall = callChain.forwardChainIterator().next();
            // just add the call chain which start with public method
            trimCallChainList.add(callChain);
        }

        // sort by the call chain length
        trimCallChainList = trimCallChainList.stream().sorted((Comparator.comparingInt(MethodCallChain::length))).collect(Collectors.toList());

        this.vulCallChainList = trimCallChainList;
    }

    // wrap sootMethod to MethodCall
    private MethodCall wrapMethod(SootMethod sootMethod) {
        if (sootMethod == null) return null;
        String signature = sootMethod.getSignature();
        return ClientProjectProperty.getMethodCallMap().getOrDefault(signature, null);
    }

    private Boolean isVulMethod(MethodCall methodCall) {
        return vulMethodSignatureSet.contains(methodCall.getMethodSignature());
    }

    public List<MethodCallChain> getVulCallChainList() {
        return vulCallChainList;
    }

    public List<MethodCallChain> getPuringVulCallChainList() {
        return puringVulCallChainList;
    }
}
