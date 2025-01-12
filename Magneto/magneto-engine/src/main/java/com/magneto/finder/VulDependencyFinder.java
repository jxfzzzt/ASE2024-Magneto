package com.magneto.finder;

import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import com.magneto.dependency.MavenDependency;
import com.magneto.dependency.MavenDependencyChain;
import com.magneto.dependency.MavenDependencyTree;
import com.magneto.testcase.TestCaseService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class VulDependencyFinder {

    private static final TestCaseService TEST_CASE_SERVICE = SpringUtil.getBean(TestCaseService.class);

    private static final Set<Pair<String, String>> BAN_DEPENDENCY_SET = new HashSet<>();

    private final MavenDependencyTree dependencyTree;

    private final Set<String> vulDependencyJarPathSet;

    private final Set<MavenDependency> vulDependencySet;

    private final List<MavenDependencyChain> vulDependencyChainList;

    // to avoid soot analysis fail
    static {
        BAN_DEPENDENCY_SET.add(new Pair<>("org.apache.hadoop", "hadoop-common"));
        BAN_DEPENDENCY_SET.add(new Pair<>("org.apache.hadoop", "hadoop-hdfs-client"));
    }

    public VulDependencyFinder(MavenDependencyTree dependencyTree) {
        this.dependencyTree = dependencyTree;
        this.vulDependencySet = new HashSet<>();
        this.vulDependencyJarPathSet = new HashSet<>();
        this.vulDependencyChainList = new ArrayList<>();
        depthSearchFindVulChain(dependencyTree.getRootNode(), vulDependencyChainList, new ArrayList<>());
    }

    // DFS search dependency chain
    private void depthSearchFindVulChain(MavenDependency node, List<MavenDependencyChain> chainList, List<MavenDependency> currentDependencyPath) {
        if (!currentDependencyPath.isEmpty() && checkDependencyBanned(node)) {
            return;
        }

        currentDependencyPath.add(node);

        // if is a vulnerable dependency
        if (checkDependencyStatus(node)) {
            this.vulDependencySet.add(node);
            // add dependencyChain
            chainList.add(new MavenDependencyChain(new ArrayList<>(currentDependencyPath)));
            // storage vulnerable dependencyChain jar file
            for (MavenDependency dependency : currentDependencyPath) {
                if (!dependency.equals(dependencyTree.getRootNode())) {
                    String jarPath = this.dependencyTree.getDependencyJarPathMap().get(dependency);
                    this.vulDependencyJarPathSet.add(jarPath);
                }
            }
        }

        // dfs search dependency tree
        for (MavenDependency dependency : node.getDependencies()) {
            depthSearchFindVulChain(dependency, chainList, currentDependencyPath);
        }
        currentDependencyPath.remove(currentDependencyPath.size() - 1);
    }

    private Boolean checkDependencyBanned(MavenDependency dependency) {
        for (Pair<String, String> pair : BAN_DEPENDENCY_SET) {
            if (pair.getKey().equals(dependency.getGroupId()) && pair.getValue().equals(dependency.getArtifactId())) {
                return true;
            }
        }
        return false;
    }

    public List<MavenDependencyChain> getVulDependencyChainList() {
        return vulDependencyChainList;
    }

    public List<String> getVulDependencyJarPathList() {
        return new ArrayList<>(this.vulDependencyJarPathSet);
    }

    private Boolean checkDependencyStatus(MavenDependency dependency) {
        Set<MavenDependency> vulDependencySet = TEST_CASE_SERVICE.getVulDependencySet();
        return vulDependencySet.contains(dependency);
    }

    public Set<MavenDependency> getVulDependencySet() {
        return vulDependencySet;
    }
}
