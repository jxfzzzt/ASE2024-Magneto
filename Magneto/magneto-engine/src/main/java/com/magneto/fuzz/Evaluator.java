package com.magneto.fuzz;

import com.magneto.fuzz.result.InvokeMethodResult;
import com.magneto.fuzz.runner.MethodInvokeWrapper;
import com.magneto.fuzz.seed.Seed;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.util.MathUtil;
import com.magneto.util.object.ObjectUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Evaluator {

    private final static long TIME_OUT = 90000L;

    private final static TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    public static void evaluateSeed(@NonNull Seed seed, ClassLoader loader,
                                    MethodCall targetMethodCall, MethodCall triggeredMethodCall, InvokeMethodResult invokeResult) {
        try {
            evaluateDistanceAndCoverageScore(seed, loader, targetMethodCall, triggeredMethodCall, invokeResult);
        } catch (Exception e) {
            log.warn("EvaluateSeedError: ", e);
        }
        seed.setEvaluateStatus(true);
    }

    private static void evaluateDistanceAndCoverageScore(Seed seed, ClassLoader loader,
                                                         MethodCall targetMethodCall, MethodCall triggeredMethodCall,
                                                         InvokeMethodResult invokeResult) throws Exception {

        LineNumberInfo lineNumberInfo = analysisLineNumberInfo(targetMethodCall.getSootMethod(), triggeredMethodCall.getSootMethod());
        final IRuntime runtime = new LoggerRuntime();
        final Instrumenter instr = new Instrumenter(runtime);

        // get target class inputStream
        String className = targetMethodCall.getClazz().getName();
        InputStream original = getTargetClass(loader, className);

        assert original != null;

        final byte[] instrumented = instr.instrument(original, className);
        original.close();

        final RuntimeData data = new RuntimeData();
        runtime.startup(data);

        final MemoryClassLoader memoryClassLoader = new MemoryClassLoader(loader);
        memoryClassLoader.addDefinition(className, instrumented);
        final Class<?> targetClass = memoryClassLoader.loadClass(className);

        Object o;
        if (seed.getObject() != null) {
            o = ObjectUtil.newObject(targetClass);
            ObjectUtil.copyObjectField(o, seed.getObject());
        } else {
            o = null;
        }

        Class[] types = targetMethodCall.getParameterTypes();

        Method method = targetClass.getDeclaredMethod(targetMethodCall.getMethodName(), types);
        method.setAccessible(true);

        try {
            new MethodInvokeWrapper(
                    () -> method.invoke(o, seed.getParamValues()), TIME_OUT, TIME_UNIT)
                    .invoke();
        } catch (IllegalArgumentException e) {
            log.warn("method invoke input illegal exception");
            throw e;
        }

        final ExecutionDataStore executionData = new ExecutionDataStore();
        final SessionInfoStore sessionInfos = new SessionInfoStore();
        data.collect(executionData, sessionInfos, false);
        runtime.shutdown();

        final CoverageBuilder coverageBuilder = new CoverageBuilder();
        final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
        original = getTargetClass(loader, className);
        analyzer.analyzeClass(original, className);
        original.close();

        List<Integer> execLines = new ArrayList<>();
        int lineNumberCount = 0;

        for (IClassCoverage classCoverage : coverageBuilder.getClasses()) {
            String name = classCoverage.getName().replace("/", ".");
            if (className.equals(name)) {
                for (int i = classCoverage.getFirstLine(); i <= classCoverage.getLastLine(); i++) {
                    if (i >= lineNumberInfo.getMethodStartLineNumber() && i <= lineNumberInfo.getMethodEndLineNumber()) {
                        ILine line = classCoverage.getLine(i);
                        if (ICounter.EMPTY == line.getStatus()) continue;
                        lineNumberCount++;
                        if (ICounter.FULLY_COVERED == line.getStatus() || ICounter.PARTLY_COVERED == line.getStatus()) {
                            execLines.add(i);
                        }
                    }
                }
            }
        }

        // evaluate coverage
        double coverage = 1.0D * execLines.size() / lineNumberCount;
        seed.setCoverageScore(coverage);

        assert !lineNumberInfo.getTriggeredMethodLineNumbers().isEmpty();

        // evaluate distance
        List<Double> distList = new ArrayList<>();
        for (Integer execLine : execLines) {
            double dist = 0.0;
            boolean hasCalc = false;
            int cnt = 0;
            for (Integer triggeredLineNumber : lineNumberInfo.getTriggeredMethodLineNumbers()) {
                if (execLine <= triggeredLineNumber) {
                    hasCalc = true;
                    dist += (triggeredLineNumber - execLine);
                    cnt++;
                }
            }
            if (hasCalc) {
                dist = dist / (1.0 * cnt);
                distList.add(dist);
            }
        }

        Double minValue = MathUtil.min(distList);
        Double maxValue = MathUtil.max(distList);
        Double distanceScore;

        if (!minValue.equals(maxValue)) {
            double dist = 0.0;
            for (Double v : distList) {
                dist += (v - minValue) / (maxValue - minValue);
            }
            dist /= distList.size();

            assert dist >= 0 && dist <= 1;
            distanceScore = 1 - dist;
        } else {
            distanceScore = 0.3;
        }

//        if (ResultStatus.Reached.equals(invokeResult.getResultStatus())) {
//            distanceScore = Math.min(1.0D, distanceScore * 1.25);
//        } else {
//            distanceScore = Math.max(0.2D, distanceScore * 0.8);
//        }

        seed.setDistanceScore(distanceScore);
    }

    private static InputStream getTargetClass(final ClassLoader loader, final String className) {
        String classResourceName = className.replace('.', '/') + ".class";
        return loader.getResourceAsStream(classResourceName);
    }

    private static class MemoryClassLoader extends ClassLoader {
        private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

        private final ClassLoader loader;

        public MemoryClassLoader(ClassLoader loader) {
            this.loader = loader;
        }

        public void addDefinition(final String name, final byte[] bytes) {
            definitions.put(name, bytes);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            final byte[] bytes = definitions.get(name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return loader.loadClass(name);
        }

    }


    private static LineNumberInfo analysisLineNumberInfo(SootMethod targetMethod, SootMethod triggeredMethod) {
        LineNumberInfo lineNumberInfo = new LineNumberInfo();

        Body body = targetMethod.retrieveActiveBody();
        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;
            int lineNumber = stmt.getJavaSourceStartLineNumber();
            if (lineNumber != -1) {
                Integer startLineNumber = Math.min(lineNumberInfo.getMethodStartLineNumber(), lineNumber);
                Integer endLineNumber = Math.max(lineNumberInfo.getMethodEndLineNumber(), lineNumber);
                lineNumberInfo.setMethodStartLineNumber(startLineNumber);
                lineNumberInfo.setMethodEndLineNumber(endLineNumber);
                lineNumberInfo.addLineNumber(lineNumber);
            }

            if (stmt.containsInvokeExpr()) {
                if (stmt instanceof JAssignStmt) {
                    JAssignStmt jAssignStmt = (JAssignStmt) stmt;
                    Value rightOp = jAssignStmt.getRightOp();
                    if (rightOp instanceof InvokeExpr) {
                        InvokeExpr invokeExpr = (InvokeExpr) rightOp;
                        SootMethod method = invokeExpr.getMethod();
                        if (method.getSignature().equals(triggeredMethod.getSignature())) {
                            lineNumberInfo.addTriggeredLineNumber(lineNumber);
                        }
                    }
                } else if (stmt instanceof JInvokeStmt) {
                    JInvokeStmt jInvokeStmt = (JInvokeStmt) stmt;
                    SootMethod method = jInvokeStmt.getInvokeExpr().getMethod();
                    if (method.getSignature().equals(triggeredMethod.getSignature())) {
                        lineNumberInfo.addTriggeredLineNumber(lineNumber);
                    }
                }
            }
        }

        return lineNumberInfo;
    }

    private static class LineNumberInfo {
        private Integer methodStartLineNumber;

        private Integer methodEndLineNumber;

        private List<Integer> triggeredMethodLineNumbers;

        private Set<Integer> allLineNumberSet;

        public LineNumberInfo() {
            this.triggeredMethodLineNumbers = new ArrayList<>();
            this.allLineNumberSet = new HashSet<>();
            this.methodStartLineNumber = Integer.MAX_VALUE;
            this.methodEndLineNumber = Integer.MIN_VALUE;
        }

        public void addTriggeredLineNumber(Integer line) {
            triggeredMethodLineNumbers.add(line);
        }

        public void addLineNumber(Integer line) {
            allLineNumberSet.add(line);
        }

        public Integer getMethodStartLineNumber() {
            return methodStartLineNumber;
        }

        public List<Integer> getTriggeredMethodLineNumbers() {
            return triggeredMethodLineNumbers;
        }

        public Integer getMethodEndLineNumber() {
            return methodEndLineNumber;
        }

        public Set<Integer> getAllLineNumberSet() {
            return allLineNumberSet;
        }

        public void setMethodStartLineNumber(Integer methodStartLineNumber) {
            this.methodStartLineNumber = methodStartLineNumber;
        }

        public void setMethodEndLineNumber(Integer methodEndLineNumber) {
            this.methodEndLineNumber = methodEndLineNumber;
        }

        public void setAllLineNumberSet(Set<Integer> allLineNumberSet) {
            this.allLineNumberSet = allLineNumberSet;
        }

        public void setTriggeredMethodLineNumbers(List<Integer> triggeredMethodLineNumbers) {
            this.triggeredMethodLineNumbers = triggeredMethodLineNumbers;
        }
    }


}
