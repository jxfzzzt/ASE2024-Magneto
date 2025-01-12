package com.magneto;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import com.magneto.config.*;
import com.magneto.dependency.MavenDependency;
import com.magneto.dependency.MavenDependencyTree;
import com.magneto.finder.VulCallChainFinder;
import com.magneto.finder.VulDependencyFinder;
import com.magneto.fuzz.FuzzClassLoader;
import com.magneto.fuzz.result.FuzzChainResultWrapper;
import com.magneto.fuzz.runner.StepFuzzRunner;
import com.magneto.gpt.GPTSolver;
import com.magneto.report.ReportGenerator;
import com.magneto.report.html.HTMLReportGenerator;
import com.magneto.report.json.JSONReportGenerator;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;
import com.magneto.staticanalysis.PropertyAnalysis;
import com.magneto.staticanalysis.callgraph.ProjectCallGraph;
import com.magneto.testcase.TestCaseService;
import com.magneto.util.DecompileUtil;
import com.magneto.util.TimeUtil;
import com.magneto.util.URLUtil;
import com.magneto.util.output.JsonOutputHandler;
import com.magneto.util.output.OutputHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

@Slf4j
@Import(cn.hutool.extra.spring.SpringUtil.class)
@Component
@ComponentScan(basePackages = {"cn.hutool.extra.spring"})
public class Magneto {
    @Autowired
    TestCaseService testCaseService;

    @Autowired
    ConfigProperty configProperty;

    public static final String PROJECT_ROOT_PATH = "p";

    public static final String SKIP_FUZZING = "skipFuzz";

    public static final String TARGET_VULNERABILITY = "vul";

    public static final String OUTPUT_DIR = "output";

    private static final OutputHandler outputHandler = new JsonOutputHandler();

    private void prepareOutputDir() {
        FileUtil.del(GlobalConfiguration.OUTPUT_DIR);
        FileUtil.mkdir(GlobalConfiguration.OUTPUT_DIR);
    }

    private void prepareCacheDir() {
        FileUtil.del(GlobalConfiguration.CACHE_DIR_PATH);
        FileUtil.mkdir(GlobalConfiguration.CACHE_DIR_PATH);
        FileUtil.mkdir(GlobalConfiguration.JAR_DIR_PATH);
    }

    private MavenDependencyTree generateDependencyTree(ProjectContext projectContext) {
        return TimeUtil.runTask(() -> {
            MavenDependencyTree dependencyTree = null;
            try {
                dependencyTree = new MavenDependencyTree(projectContext.getProjectDir());
            } catch (Exception e) {
                log.error("ParsingMavenDependencyTreeError: ", e);
                throw new RuntimeException(e);
            }

            projectContext.setDependencyTree(dependencyTree);
            return dependencyTree;
        }, "generate-dependency-tree");
    }

    public void initGlobalClassLoader(ProjectContext projectContext) {
        TimeUtil.runTask(() -> {
            MavenDependencyTree dependencyTree = projectContext.getDependencyTree();

            List<String> dependencyJarList = Arrays.stream(FileUtil.ls(GlobalConfiguration.JAR_DIR_PATH))
                    .filter(File::isFile)
                    .map(File::getAbsolutePath).filter(s -> s.endsWith(".jar")).collect(Collectors.toList());

            String[] jarPaths = new String[1 + dependencyJarList.size()];
            jarPaths[0] = dependencyTree.getJarFile().getAbsolutePath();
            for (int i = 0; i < dependencyJarList.size(); i++) {
                jarPaths[i + 1] = dependencyJarList.get(i);
            }
            try {
                GlobalClassLoader.init(jarPaths);
            } catch (MalformedURLException e) {
                log.error("GlobalClassLoaderInitError: ", e);
                throw new RuntimeException(e);
            }
            // switch the global class loader
            Thread.currentThread().setContextClassLoader(GlobalClassLoader.getInstance().getClassLoader());
        }, "init-global-classloader");
    }

    private void initializeFuzzEnv(ProjectContext projectContext) {
        TimeUtil.runTask(() -> {
            MavenDependencyTree dependencyTree = projectContext.getDependencyTree();
            List<String> jarPathList = dependencyTree.getDependencyJarPathList();
            jarPathList.add(dependencyTree.getJarFile().getAbsolutePath());

            ClassLoader instrumentedFuzzLoader = null;
            ClassLoader fuzzLoader = null;
            try {
                instrumentedFuzzLoader = new FuzzClassLoader(jarPathList);
                fuzzLoader = new URLClassLoader(URLUtil.stringsToUrls(jarPathList), ClassLoader.getSystemClassLoader().getParent());
            } catch (MalformedURLException e) {
                log.error("InitializeFuzzEnvError: ", e);
                throw new RuntimeException(e);
            }

            ClientProjectProperty.setFuzzLoader(fuzzLoader);
            ClientProjectProperty.setInstrumentedFuzzLoader(instrumentedFuzzLoader);
            projectContext.setInstrumentedFuzzClassLoader(instrumentedFuzzLoader);
            projectContext.setFuzzClassLoader(fuzzLoader);

        }, "initialize-fuzzing-environment");
    }

    private VulDependencyFinder generateVulDependencyFinder(ProjectContext projectContext) {
        return TimeUtil.runTask(() -> {
            MavenDependencyTree dependencyTree = projectContext.getDependencyTree();
            VulDependencyFinder vulDependencyFinder = new VulDependencyFinder(dependencyTree);
            projectContext.setVulDependencyFinder(vulDependencyFinder);
            return vulDependencyFinder;
        }, "generate-vulnerable-dependency-chain");
    }

    private PropertyAnalysis initClientProjectProperty(ProjectContext projectContext) {
        return TimeUtil.runTask(() -> {
            MavenDependencyTree dependencyTree = projectContext.getDependencyTree();
            VulDependencyFinder vulDependencyFinder = projectContext.getVulDependencyFinder();
            // vulnerable jar file path
            List<String> vulJarPathList = vulDependencyFinder.getVulDependencyJarPathList();

            PropertyAnalysis propertyAnalysis = null;
            try {
                // just analysis the vulnerability related jar files
                propertyAnalysis = PropertyAnalysis.getInstance(dependencyTree, dependencyTree.getJarFile().getAbsolutePath(), vulJarPathList);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            projectContext.setSootAnalysis(propertyAnalysis);
            return propertyAnalysis;
        }, "initialize-client-project-property");
    }

    private ProjectCallGraph generateCallGraph(ProjectContext projectContext) {
        return TimeUtil.runTask(() -> {
            ProjectCallGraph projectCallGraph = null;

            MavenDependencyTree dependencyTree = projectContext.getDependencyTree();
            VulDependencyFinder vulDependencyFinder = projectContext.getVulDependencyFinder();
            // vulnerable jar file path
            List<String> vulJarPathList = vulDependencyFinder.getVulDependencyJarPathList();

            try {
                projectCallGraph = new ProjectCallGraph(dependencyTree.getJarFile().getAbsolutePath(), vulJarPathList);
            } catch (IOException | ClassNotFoundException e) {
                log.error("GenerateCallGraphError: ", e);
                throw new RuntimeException(e);
            }

            projectContext.setProjectCallGraph(projectCallGraph);
            return projectCallGraph;
        }, "generate-methodcall-graph");
    }

    private void decompileJarFile(ProjectContext projectContext) {
        TimeUtil.runTask(() -> {
            VulCallChainFinder vulCallChainFinder = projectContext.getVulCallChainFinder();
            List<MethodCallChain> puringVulCallChainList = vulCallChainFinder.getPuringVulCallChainList();

            Set<MavenDependency> dependencySet = new HashSet<>();
            for (MethodCallChain methodCallChain : puringVulCallChainList) {
                List<MethodCall> methodCalls = methodCallChain.forwardChainList();
                for (MethodCall methodCall : methodCalls) {
                    MavenDependency dependency = methodCall.getDependency();

                    assertNotNull(dependency);

                    dependencySet.add(dependency);
                }
            }

            for (MavenDependency dependency : dependencySet) {
                try {
                    DecompileUtil.decompileDependency(dependency);
                } catch (Exception ignored) {
                }
            }
        }, "decompile-jar-files");
    }

    private VulCallChainFinder generateVulCallChainFinder(ProjectContext projectContext) {
        return TimeUtil.runTask(() -> {
            ProjectCallGraph projectCallGraph = projectContext.getProjectCallGraph();
            VulDependencyFinder vulDependencyFinder = projectContext.getVulDependencyFinder();
            VulCallChainFinder vulCallChainFinder = new VulCallChainFinder(projectCallGraph, vulDependencyFinder);
            projectContext.setVulCallChainFinder(vulCallChainFinder);
            return vulCallChainFinder;
        }, "generate-vulnerable-methodcall-chain");
    }

    private void outputStaticAnalysisResult(ProjectContext projectContext) {
        TimeUtil.runTask(() -> {
            // output vulnerable dependency chain
            VulDependencyFinder vulDependencyFinder = projectContext.getVulDependencyFinder();
            outputHandler.outputDependencyChain(vulDependencyFinder.getVulDependencyChainList());

            // output vulnerable method call chain
            VulCallChainFinder vulCallChainFinder = projectContext.getVulCallChainFinder();
            outputHandler.outputMethodCallChain(vulCallChainFinder.getVulCallChainList());
            outputHandler.outputMethodCallChain("puring-method-call-chain.json", vulCallChainFinder.getPuringVulCallChainList());
        }, "output-static-analysis-result");
    }

    private List<FuzzChainResultWrapper> fuzzVulCallChain(ProjectContext projectContext) {
        return TimeUtil.runTask(() -> {
            VulCallChainFinder vulCallChainFinder = projectContext.getVulCallChainFinder();
            List<MethodCallChain> vulCallChainList = vulCallChainFinder.getPuringVulCallChainList();

            StepFuzzRunner fuzzRunner = new StepFuzzRunner(projectContext);

            List<FuzzChainResultWrapper> fuzzChainResults = new ArrayList<>();
            try {
                // fuzzing each method call chain
                for (int i = 0; i < vulCallChainList.size() && i < configProperty.getMaxFuzzChainNumber(); i++) {
                    log.info("[ begin to fuzzing {}th method call chain ]", i + 1);
                    MethodCallChain methodCallChain = vulCallChainList.get(i);
                    FuzzChainResultWrapper fuzzChainResultWrapper = fuzzRunner.fuzzCallChain(methodCallChain);
                    fuzzChainResults.add(fuzzChainResultWrapper);
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("FuzzingMethodCallChainError: ", e);
                throw new RuntimeException(e);
            }
            projectContext.setFuzzChainResults(fuzzChainResults);

            // output gpt answer
            GPTSolver.SaveGPTAnswerCache(FileUtil.file(GlobalConfiguration.OUTPUT_DIR_PATH, "gpt-cache.json"));

            return fuzzChainResults;
        }, "fuzzing-vulnerable-methodcall-chain");
    }

    private void generateFuzzReport(ProjectContext projectContext) {
        TimeUtil.runTask(() -> {
            try {
                ReportGenerator generator = new HTMLReportGenerator();
                generator.generate(projectContext, projectContext.getFuzzChainResults());

                generator = new JSONReportGenerator();
                generator.generate(projectContext, projectContext.getFuzzChainResults());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "generate-fuzzing-report");
    }

    public void work(ProjectContext projectContext) {
        TimeUtil.runTask(() -> {
            TimeInterval timer = DateUtil.timer();
            timer.start("total-task");
            timer.start("analysis-task");

            // generate dependency tree
            generateDependencyTree(projectContext);

            // init global classloader
            initGlobalClassLoader(projectContext);

            //prepare fuzzing environment
            initializeFuzzEnv(projectContext);

            // find vulnerable dependency chain
            generateVulDependencyFinder(projectContext);

            // init ClientProjectProperty
            initClientProjectProperty(projectContext);

            // generate call graph, start static analysis
            generateCallGraph(projectContext);

            // find vulnerable method call chain
            generateVulCallChainFinder(projectContext);

            long analysisTime = timer.interval("analysis-task");
            projectContext.setAnalysisTime(analysisTime);

            // output static analysis result
            outputStaticAnalysisResult(projectContext);

            timer.start("fuzz-task");

            if (!projectContext.getCommandLine().hasOption(SKIP_FUZZING)) { // iff has the -skipFuzz option, then skip fuzz step
                //decompile jar files
                decompileJarFile(projectContext);

                // fuzzing method call chain
                fuzzVulCallChain(projectContext);
            }

            long fuzzTime = timer.interval("fuzz-task");
            projectContext.setFuzzTime(fuzzTime);
            long totalTime = timer.interval("total-task");
            projectContext.setTotalTime(totalTime);

            if (!projectContext.getCommandLine().hasOption(SKIP_FUZZING)) {
                // generate fuzzing report
                generateFuzzReport(projectContext);
            }
        }, "entire-work");
    }

    public void run(ProjectContext projectContext) {
        if (projectContext == null) {
            throw new RuntimeException("project context can't be null");
        }

        log.info("the work directory is {}", GlobalConfiguration.WORK_DIR.getAbsolutePath());
        log.info("the cache directory is {}", GlobalConfiguration.CACHE_DIR.getAbsolutePath());
        log.info("the output directory is {}", GlobalConfiguration.OUTPUT_DIR.getAbsolutePath());

        // prepare the dir
        prepareCacheDir();
        prepareOutputDir();

        // everything is OK! then begin work
        work(projectContext);
    }
}
