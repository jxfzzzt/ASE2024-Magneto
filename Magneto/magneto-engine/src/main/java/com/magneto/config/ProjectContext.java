package com.magneto.config;

import com.magneto.dependency.MavenDependency;
import com.magneto.dependency.MavenDependencyChain;
import com.magneto.dependency.MavenDependencyTree;
import com.magneto.finder.VulCallChainFinder;
import com.magneto.finder.VulDependencyFinder;
import com.magneto.fuzz.result.FuzzChainResultWrapper;
import com.magneto.staticanalysis.PropertyAnalysis;
import com.magneto.staticanalysis.callgraph.ProjectCallGraph;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectContext {

    private Long totalTime;

    private Long analysisTime;

    private Long fuzzTime;

    private String projectPath;

    private File projectDir;

    private CommandLine commandLine;

    private ClassLoader instrumentedFuzzClassLoader;

    private ClassLoader fuzzClassLoader;

    private MavenDependencyTree dependencyTree;

    private ProjectCallGraph projectCallGraph;

    private VulDependencyFinder vulDependencyFinder;

    private VulCallChainFinder vulCallChainFinder;

    private PropertyAnalysis propertyAnalysis;

    private List<FuzzChainResultWrapper> fuzzChainResults;

    public ProjectContext() {

    }

    public ProjectContext(String projectPath, File projectDir, CommandLine commandLine) {
        this.projectPath = projectPath;
        this.projectDir = projectDir;
        this.commandLine = commandLine;
    }

    public List<MavenDependency> getVulDependency() {
        List<MavenDependency> dependencyList = new ArrayList<>();
        List<MavenDependencyChain> vulDependencyChainList = this.vulDependencyFinder.getVulDependencyChainList();
        for (MavenDependencyChain dependencyChain : vulDependencyChainList) {
            MavenDependency vulDependency = dependencyChain.getVulDependency();
            dependencyList.add(vulDependency);
        }
        return dependencyList;
    }

    public void setFuzzClassLoader(ClassLoader fuzzClassLoader) {
        this.fuzzClassLoader = fuzzClassLoader;
    }

    public ClassLoader getFuzzClassLoader() {
        return fuzzClassLoader;
    }

    public ClassLoader getInstrumentedFuzzClassLoader() {
        return instrumentedFuzzClassLoader;
    }

    public void setInstrumentedFuzzClassLoader(ClassLoader instrumentedFuzzClassLoader) {
        this.instrumentedFuzzClassLoader = instrumentedFuzzClassLoader;
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    public File getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(File projectDir) {
        this.projectDir = projectDir;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public void setSootAnalysis(PropertyAnalysis propertyAnalysis) {
        this.propertyAnalysis = propertyAnalysis;
    }

    public MavenDependencyTree getDependencyTree() {
        return dependencyTree;
    }

    public VulCallChainFinder getVulCallChainFinder() {
        return vulCallChainFinder;
    }

    public VulDependencyFinder getVulDependencyFinder() {
        return vulDependencyFinder;
    }

    public List<FuzzChainResultWrapper> getFuzzChainResults() {
        return fuzzChainResults;
    }

    public void setFuzzChainResults(List<FuzzChainResultWrapper> fuzzChainResults) {
        this.fuzzChainResults = fuzzChainResults;
    }

    public void setDependencyTree(MavenDependencyTree dependencyTree) {
        this.dependencyTree = dependencyTree;
    }

    public void setProjectCallGraph(ProjectCallGraph projectCallGraph) {
        this.projectCallGraph = projectCallGraph;
    }

    public void setVulCallChainFinder(VulCallChainFinder vulCallChainFinder) {
        this.vulCallChainFinder = vulCallChainFinder;
    }

    public void setVulDependencyFinder(VulDependencyFinder vulDependencyFinder) {
        this.vulDependencyFinder = vulDependencyFinder;
    }

    public ProjectCallGraph getProjectCallGraph() {
        return projectCallGraph;
    }

    public PropertyAnalysis getSootAnalysis() {
        return propertyAnalysis;
    }

    public Long getAnalysisTime() {
        return analysisTime;
    }

    public Long getFuzzTime() {
        return fuzzTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setAnalysisTime(Long analysisTime) {
        this.analysisTime = analysisTime;
    }

    public void setFuzzTime(Long fuzzTime) {
        this.fuzzTime = fuzzTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }
}
