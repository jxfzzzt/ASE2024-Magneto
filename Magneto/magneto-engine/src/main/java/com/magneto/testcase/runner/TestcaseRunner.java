package com.magneto.testcase.runner;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson2.JSON;
import com.magneto.config.GlobalConfiguration;
import com.magneto.fuzz.FuzzClassLoader;
import com.magneto.fuzz.runner.MethodInvokeWrapper;
import com.magneto.instrument.state.StateNode;
import com.magneto.testcase.model.MetaInfo;
import com.magneto.testcase.model.TestcaseUnit;
import com.magneto.testcase.parser.POMParser;
import com.magneto.util.CommandUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class TestcaseRunner {

    protected final static long TIME_OUT = 90000L;

    protected final static TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private static final String METAINFO_FILE_NAME = "metainfo.json";

    private static final String POM_FILE_NAME = "pom.xml";

    public static final String DEPENDENCY_DIR_NAME = "dependency";

    private final String testcaseDirPath;

    private final File testcaseDir;

    private POMParser pomParser;

    private MetaInfo metaInfo;

    private File targetDir;

    private File classesDir;

    private File dependencyDir;

    public TestcaseRunner(@NonNull String testcaseDirPath) {
        this.testcaseDir = FileUtil.file(testcaseDirPath);
        if (this.testcaseDir.isDirectory()) {
            // do nothing
        } else {
            throw new RuntimeException("the input is not directory");
        }
        this.testcaseDirPath = testcaseDir.getAbsolutePath();
        readDataFromMetaInfo();
        createPOMParser();
    }

    public TestcaseRunner(@NonNull File testcaseDir) {
        this.testcaseDir = testcaseDir;
        if (this.testcaseDir.isDirectory()) {
            // do nothing
        } else {
            throw new RuntimeException("the input is not directory");
        }
        this.testcaseDirPath = this.testcaseDir.getAbsolutePath();
        readDataFromMetaInfo();
        createPOMParser();
    }

    private void createPOMParser() {
        File pomFile = FileUtil.file(testcaseDirPath, POM_FILE_NAME);
        this.pomParser = new POMParser(pomFile);
    }

    private void readDataFromMetaInfo() {
        File file = FileUtil.file(testcaseDirPath, METAINFO_FILE_NAME);
        if (file.isFile()) {
            FileReader reader = new FileReader(file);
            this.metaInfo = JSON.parseObject(reader.readString(), MetaInfo.class);
        } else {
            throw new RuntimeException("the metainfo file not exist.");
        }
    }

    private boolean compileTestcaseProject(String version) throws IOException, InterruptedException {
        // modify the library version
        pomParser.updatePOMFileVersion(metaInfo.getGroupId(), metaInfo.getArtifactId(), version);

        // compile testcase maven project
        this.targetDir = FileUtil.file(testcaseDir, "target");
        if (this.targetDir.exists()) {
            FileUtil.del(this.targetDir);
        }
        this.classesDir = FileUtil.file(targetDir, "classes");
        this.dependencyDir = FileUtil.file(targetDir, DEPENDENCY_DIR_NAME);
        CommandUtil.execCommand(testcaseDir, GlobalConfiguration.MAVEN_EXEC_COMMAND, false);

        if (targetDir.exists() && targetDir.isDirectory()
                && classesDir.exists() && classesDir.isDirectory()
                && dependencyDir.exists() && dependencyDir.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public ClassLoader getTestcaseClassLoader() throws IOException {
        List<String> paths = Files.walk(Paths.get(dependencyDir.getAbsolutePath()))
                .map(Path::toString).collect(Collectors.toList());
        paths.add(classesDir.getAbsolutePath());

        // Create instrumenting testcase class loader
        return new FuzzClassLoader(paths);
    }

    private TestcaseUnit findTestcaseUnit(@NonNull String className) {
        for (TestcaseUnit testcaseUnit : metaInfo.getTestcaseUnitList()) {
            if (className.equals(testcaseUnit.getTestcaseClassName())) {
                return testcaseUnit;
            }
        }
        return null;
    }

    private void beforeRunTestcase(ClassLoader loader, String configDirPath) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Thread.currentThread().setContextClassLoader(loader);
        System.setProperty("user.dir", configDirPath);

        // reset the stateNode table
        Class<?> tableClass = loader.loadClass(FuzzClassLoader.STATE_TABLE_CLASS_NAME);
        Method resetMethod = tableClass.getDeclaredMethod("reset");
        resetMethod.invoke(null);
    }

    private void afterRunTestcase(FuzzClassLoader fuzzLoader, ClassLoader currentLoader, String configDirPath, RunResult runResult) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Thread.currentThread().setContextClassLoader(currentLoader);
        System.setProperty("user.dir", configDirPath);

        // get the stateNode table
        Class<?> tableClass = fuzzLoader.loadClass(FuzzClassLoader.STATE_TABLE_CLASS_NAME);
        Class<?> nodeClass = fuzzLoader.loadClass(FuzzClassLoader.STATE_NODE_CLASS_NAME);

        Method getStateTableMethod = tableClass.getDeclaredMethod("getStateTable");
        Method getSignatureMethod = nodeClass.getDeclaredMethod("getSignature");
        Method getValueMethod = nodeClass.getDeclaredMethod("getValue");
        Method getClazzMethod = nodeClass.getDeclaredMethod("getClazz");

        Map<String, StateNode> copyStateNode = new HashMap<>();

        Map<String, StateNode> stateNodeMap = (Map<String, StateNode>) getStateTableMethod.invoke(null);
        for (String key : stateNodeMap.keySet()) {
            Object stateNode = stateNodeMap.get(key);
            String signature = (String) getSignatureMethod.invoke(stateNode);
            Object value = getValueMethod.invoke(stateNode);
            Class<?> clazz = (Class<?>) getClazzMethod.invoke(stateNode);

            StateNode copyNode = new StateNode(signature, clazz, value);
            copyStateNode.put(key, copyNode);
        }
        runResult.setRunStateTable(copyStateNode);

        Method getMethodSignatureSet = tableClass.getDeclaredMethod("getMethodSignatureSet");
        Set<String> runMethodSigSet = (Set<String>) getMethodSignatureSet.invoke(null);
        runResult.setRunMethodSigSet(runMethodSigSet);
    }

    public RunResult runTestcaseWithVersion(@NonNull FuzzClassLoader fuzzLoader, @NonNull String className, @NonNull String version)
            throws Exception {
        RunResult runResult = new RunResult();
        TestcaseUnit testcaseUnit = findTestcaseUnit(className);

        if (testcaseUnit == null) {
            throw new RuntimeException("the className '" + className + "' not exist");
        }

        runResult.setTestcaseUnit(testcaseUnit);
        boolean compileSuccess = compileTestcaseProject(version);
        if (compileSuccess) {
            ClassLoader testcaseLoader = getTestcaseClassLoader();
            fuzzLoader.setTestcaseLoader(testcaseLoader);

            // current state
            ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
            String currentUserDir = System.getProperty("user.dir");

            // switch the context
            beforeRunTestcase(fuzzLoader, testcaseDirPath);

            try {
                // run the testcase method
                Class<?> clazz = fuzzLoader.loadClass(className);
                Method testcaseMethod = clazz.getDeclaredMethod(testcaseUnit.getTestcaseMethodName());
                testcaseMethod.setAccessible(true);

                new MethodInvokeWrapper(
                        () -> testcaseMethod.invoke(clazz.newInstance()), TIME_OUT, TIME_UNIT)
                        .invoke();

            } catch (Throwable t) {
                log.error("RunTestcaseError: ", t);
            }

            // set to the origin context
            afterRunTestcase(fuzzLoader, currentLoader, currentUserDir, runResult);

            runResult.setRunStatus(RunStatus.SUCCESS_TRIGGER);
        } else {
            runResult.setRunStatus(RunStatus.COMPILE_ERROR);
        }
        return runResult;
    }

}