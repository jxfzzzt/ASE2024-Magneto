package com.magneto.fuzz.runner;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.magneto.config.ConfigProperty;
import com.magneto.config.ProjectContext;
import com.magneto.fuzz.Evaluator;
import com.magneto.fuzz.FuzzAnalysis;
import com.magneto.fuzz.FuzzClassLoader;
import com.magneto.fuzz.FuzzParser;
import com.magneto.fuzz.context.Context;
import com.magneto.fuzz.context.FuzzContext;
import com.magneto.fuzz.mapper.FuzzObjectMapper;
import com.magneto.fuzz.mutate.Mutator;
import com.magneto.fuzz.result.*;
import com.magneto.fuzz.seed.Seed;
import com.magneto.fuzz.seed.SeedQueue;
import com.magneto.gpt.GPTSolver;
import com.magneto.gpt.PromptGenerator;
import com.magneto.instrument.state.StateNode;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;
import com.magneto.staticanalysis.dataflow.ControlFlowVariableAnalysis;
import com.magneto.staticanalysis.dataflow.MethodParameterAnalysis;
import com.magneto.staticanalysis.dataflow.MethodParameterMappingAnalysis;
import com.magneto.staticanalysis.dataflow.VariableTrackingAnalysis;
import com.magneto.staticanalysis.taint.TaintField;
import com.magneto.staticanalysis.taint.TaintFlowAnalysis;
import com.magneto.staticanalysis.taint.TaintObject;
import com.magneto.staticanalysis.taint.TaintParam;
import com.magneto.testcase.model.TestcaseUnit;
import com.magneto.util.FieldUtil;
import com.magneto.util.RandomUtil;
import com.magneto.util.ReflectionUtil;
import com.magneto.util.object.ObjectUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.runners.model.TestTimedOutException;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@Slf4j
public class StepFuzzRunner extends FuzzRunner {

    private static final ConfigProperty CONFIG_PROPERTY = SpringUtil.getBean(ConfigProperty.class);

    private final ProjectContext context;

    private final FuzzClassLoader instrumentedFuzzLoader;

    private final ClassLoader fuzzLoader;

    private MethodCallChain chain;

    public StepFuzzRunner(ProjectContext context) {
        this.context = context;
        this.instrumentedFuzzLoader = (FuzzClassLoader) context.getInstrumentedFuzzClassLoader();
        this.fuzzLoader = context.getFuzzClassLoader();
    }

    @Override
    public synchronized FuzzChainResultWrapper fuzzCallChain(@NonNull MethodCallChain chain) throws IOException, ClassNotFoundException {
        this.chain = chain;

        Map<String, List<TestcaseUnit>> testcaseMap = chain.getTestcaseMap();

        FuzzChainResultWrapper fuzzChainResultWrapper = new FuzzChainResultWrapper(chain);

        // enumerate each vulnerability
        for (Map.Entry<String, List<TestcaseUnit>> entry : testcaseMap.entrySet()) { // (cve name, testcase unit)
            FuzzChainResult result = new FuzzChainResult();
            result.setFailStepNum(null);
            result.setSuccessStepNum(0);

            boolean success = true;

            String vulName = entry.getKey();

            log.info("[ {} fuzzing ]", vulName);

            List<TestcaseUnit> testcaseUnitList = entry.getValue();

            if (testcaseUnitList == null || testcaseUnitList.isEmpty()) {
                throw new RuntimeException("the testcase unit is null or empty");
            }

            // choose one testcase to fuzzing
            TestcaseUnit testcaseUnit = RandomUtil.randomChoose(testcaseUnitList);

            List<MethodCall> methodCallChain = chain.reverseChainList();

            assertTrue(methodCallChain.size() >= 2);

            log.info("[ {} context generate begin... ]", vulName);
            FuzzContext fuzzContext = new FuzzContext(instrumentedFuzzLoader, chain, testcaseUnit);
            log.info("[ {} context generate success ]", vulName);

            GPTSolver solver = new GPTSolver();

            long fuzzChainStartTime = System.currentTimeMillis();
            StringBuilder abstractMethodPrompt = new StringBuilder();

            for (int i = 1; i < methodCallChain.size(); i++) {
                try {
                    MethodCall targetMethodCall = methodCallChain.get(i);
                    Class<?> targetClass = targetMethodCall.getClazz();
                    MethodCall triggeredMethodCall = methodCallChain.get(i - 1);

                    // deal with abstract object & method
                    if (!targetMethodCall.isStatic() && Modifier.isAbstract(targetClass.getModifiers())) {
                        log.info("process the abstract method: {}", targetMethodCall.getMethodSignature());

                        String prompt = processAbstractMethod(targetMethodCall, triggeredMethodCall);
                        abstractMethodPrompt.append(prompt);
                        continue;
                    }

                    FuzzAnalysis fuzzAnalysis = this.analysisMethodCallStep(targetMethodCall, triggeredMethodCall);

                    // preCheck by static analysis
                    if (!fuzzAnalysis.preCheck(targetMethodCall, triggeredMethodCall)) {
                        result.setFailStepNum(i);
                        success = false;
                        log.warn("Previous check can not trigger. Fuzzing stop on {}th step", i);
                        break;
                    }

                    // One context propagation fuzzing
                    int tryCount = 0;
                    Pair<FuzzResult, Context> stepFuzzResult = null;
                    while (tryCount < CONFIG_PROPERTY.getMaxRetryCount() && stepFuzzResult == null) {
                        log.info("[ context propagation fuzzing start... ]");
                        stepFuzzResult = this.fuzzing(fuzzAnalysis, solver, fuzzContext,
                                targetMethodCall, triggeredMethodCall, abstractMethodPrompt);
                        tryCount++;
                        log.info("[ context propagation fuzzing end, result: {}]", (stepFuzzResult != null ? "success... " : "fail... "));
                    }

                    // clean the abstractMethodPrompt
                    abstractMethodPrompt.delete(0, abstractMethodPrompt.length());

                    if (stepFuzzResult != null) {
                        result.addFuzzResult(stepFuzzResult.getKey());
                        fuzzContext.addNewContext(stepFuzzResult.getValue());
                        result.setSuccessStepNum(i);
                    } else {
                        result.setFailStepNum(i);
                        success = false;
                        log.warn("fuzzing stop on {}th step", i);
                        break;
                    }
                } catch (Throwable e) {
                    log.error("StepFuzzError: ", e);
                    result.setFailStepNum(i);
                    success = false;
                    log.warn("fuzzing stop on {}th step", i);
                    break;
                }
            }

            result.setFuzzAllSuccess(success);
            result.setFuzzChainTime(System.currentTimeMillis() - fuzzChainStartTime);

            fuzzChainResultWrapper.addFuzzResult(vulName, result);
        }

        return fuzzChainResultWrapper;
    }

    // process the abstract method
    protected String processAbstractMethod(MethodCall targetMethodCall, MethodCall triggeredMethodCall) throws IOException {
        PromptGenerator generator = new PromptGenerator(targetMethodCall, triggeredMethodCall);
        return generator.generate(false);
    }

    protected Pair<FuzzResult, Context> fuzzing(FuzzAnalysis fuzzAnalysis, GPTSolver solver, FuzzContext fuzzContext,
                                                MethodCall targetMethodCall, MethodCall triggeredMethodCall, StringBuilder abstractMethodPrompt)
            throws Exception {
        // ask GPT to understand code
        Pair<String, String> gptAnswer = solver.getGPTAnswer(targetMethodCall, triggeredMethodCall, abstractMethodPrompt); // (query content, GPT answer)
        String value = gptAnswer.getValue();
        log.warn("the gpt answer: {}", value);

        // init fuzzing Seed
        Context latestContext = fuzzContext.getLatestContext();
        FuzzObjectMapper fuzzObjectMapper = new FuzzObjectMapper(instrumentedFuzzLoader);
        FuzzParser fuzzParser = new FuzzParser(instrumentedFuzzLoader, fuzzObjectMapper);

        // if the class of target method call is abstract or interface, it will return nullable seed
        Seed initSeed = fuzzParser.parseSeed(value, latestContext, targetMethodCall);

        if (initSeed != null) {
            // fix seed and inject the context into seed
            fixAndConnectContext(initSeed, latestContext, fuzzAnalysis, fuzzObjectMapper, targetMethodCall, triggeredMethodCall);

            SeedQueue queue = new SeedQueue();
            queue.add(initSeed);

            // begin fuzzing
            long beginTime = System.currentTimeMillis();
            long maxFuzzTime = SpringUtil.getBean(ConfigProperty.class).getMaxFuzzTime();

            while (System.currentTimeMillis() - beginTime <= maxFuzzTime) {
                // fuzz loop
                Seed seed = queue.getSeed();
                assert seed != null;

                seed.select();
                int energy = seed.getEnergy();

                for (int i = 0; i <= energy; i++) { // energy represent the mutation count
                    boolean isSelf = (i == 0);
                    Seed subSeed;

                    if (isSelf) {
                        subSeed = seed;
                        if (subSeed.getEvaluateStatus()) continue;
                    } else {
                        // mutation a new Seed
                        subSeed = Mutator.mutateSeed(seed, fuzzAnalysis, latestContext);
                    }

                    // execute the seed
                    InvokeMethodResult invokeMethodResult = this.invokeMethod(subSeed, targetMethodCall);
                    if (invokeMethodResult == null) {
                        log.warn("invokeMethodResult is null");
                        continue;
                    }

                    ValidateResult validateResult = validateInvokeResult(fuzzContext, invokeMethodResult);

                    if (validateResult.isSuccess()) {
                        // process the seed to context and fuzz result
                        log.info("*** [success trigger the vulnerability in library] ***");
                        log.info("[ {} ===> {} success ]", targetMethodCall.getMethodSubSignature(), triggeredMethodCall.getMethodSubSignature());

                        solver.updateGPTServerStatus(gptAnswer.getKey(), gptAnswer.getValue());
                        solver.addGPTAnswerCache(gptAnswer);
                        return processTriggerSeed(beginTime, targetMethodCall, subSeed,
                                gptAnswer.getKey(), gptAnswer.getValue(), validateResult, invokeMethodResult);
                    } else {
                        // evaluate seed
                        Evaluator.evaluateSeed(subSeed, instrumentedFuzzLoader, targetMethodCall, triggeredMethodCall, invokeMethodResult);

                        // if more interested, add seed to queue
                        if (subSeed.getSeedScore() >= seed.getSeedScore()) {
                            log.info("new seed push into queue");
                            queue.add(subSeed);
                        }
                    }
                }
            }

            return null;
        } else {
            return null;
        }
    }

    protected void fixAndConnectContext(Seed seed, Context context, FuzzAnalysis fuzzAnalysis, FuzzObjectMapper fuzzObjectMapper,
                                        MethodCall targetMethodCall, MethodCall triggeredMethodCall) {
        fixContext(seed, context, fuzzAnalysis, fuzzObjectMapper, targetMethodCall, triggeredMethodCall);
        connectContext(seed, context, fuzzAnalysis, targetMethodCall, triggeredMethodCall);
    }

    protected void fixContext(Seed seed, Context context, FuzzAnalysis fuzzAnalysis, FuzzObjectMapper fuzzObjectMapper,
                              MethodCall targetMethodCall, MethodCall triggeredMethodCall) {
        // fix seed params context
        Object[] paramValues = seed.getParamValues();
        Object[] contextParams = context.getParamsValue();
        Class<?>[] parameterTypes = targetMethodCall.getParameterTypes();

        for (int i = 0; i < paramValues.length; i++) {
            Integer pos = fuzzAnalysis.locateParamPos(targetMethodCall.getMethodSignature(), i);
            if (pos == -1) continue;

            TaintParam taintParam = fuzzAnalysis.getTaintParamFromMethodParams(targetMethodCall.getMethodSignature(), i);

            if (taintParam == null) continue;

            HashSet<String> attributes = taintParam.getAttributes();

            if (paramValues[i] == null ||
                    (parameterTypes[i] == String.class && ReUtil.isMatch(Pattern.compile(".*<(\\d+)>.*"), paramValues[i].toString()))) {
                // fix NULL situation or GPT wrong answer situation
                if (pos >= 0 && pos < contextParams.length && contextParams[pos] != null) {
                    if (parameterTypes[i].isAssignableFrom(contextParams[pos].getClass())) {
                        paramValues[i] = contextParams[pos];
                    } else if (parameterTypes[i].isAssignableFrom(Collection.class)) {
                        Collection collection = new ArrayList();
                        collection.add(contextParams[pos]);
                        paramValues[i] = collection;
                    } else {
                        Object o = ObjectUtil.newObject(parameterTypes[i]);
                        if (o != null && attributes != null) {
                            for (Field field : o.getClass().getDeclaredFields()) {
                                if (attributes.contains(field.getName())) {
                                    try {
                                        Object mappingValue = fuzzObjectMapper.mapping(contextParams[pos], field.getType());
                                        if (mappingValue != null && field.getType().isAssignableFrom(mappingValue.getClass())) {
                                            ReflectionUtil.setFieldValue(o, field.getName(), mappingValue);
                                        }
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                        }
                        paramValues[i] = o;
                    }
                }
            } else {
                // fix attribute forget situation
                if (pos >= 0 && pos < contextParams.length && contextParams[pos] != null) {
                    Object o = paramValues[i];

                    assert o != null;
                    if (attributes != null) {
                        for (Field field : o.getClass().getDeclaredFields()) {
                            if (attributes.contains(field.getName())) {
                                try {
                                    Object mappingValue = fuzzObjectMapper.mapping(contextParams[pos], field.getType());
                                    if (mappingValue != null && field.getType().isAssignableFrom(mappingValue.getClass())) {
                                        ReflectionUtil.setFieldValue(o, field.getName(), mappingValue);
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                    paramValues[i] = o;
                }
            }
        }

        // fix seed object context
        Object object = seed.getObject();
        if (object != null) {
            Field[] declaredFields = object.getClass().getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);

                String fieldSignature = FieldUtil.getFieldSignature(object.getClass(), field);
                Integer pos = fuzzAnalysis.locateParamPos(fieldSignature);
                if (pos == -1) continue;

                TaintField taintField = fuzzAnalysis.getTaintFieldFromMethodParams(fieldSignature);

                if (taintField == null) continue;

                HashSet<String> attributes = taintField.getAttributes();


                Object fieldValue = null;
                // get field value
                try {
                    fieldValue = ReflectionUtil.getFieldValue(object, field.getName());
                } catch (Exception e) {
                    continue;
                }

                if (fieldValue == null ||
                        (field.getType() == String.class && ReUtil.isMatch(Pattern.compile(".*<(\\d+)>.*"), fieldValue.toString()))) {
                    // fix NULL situation or GPT random answer situation
                    if (pos >= 0 && pos < contextParams.length && contextParams[pos] != null) {
                        if (field.getType().isAssignableFrom(contextParams[pos].getClass())) {
                            try {
                                ReflectionUtil.setFieldValue(object, field.getName(), contextParams[pos]);
                            } catch (Exception ignored) {
                            }
                        } else if (field.getType().isAssignableFrom(Collection.class)) {
                            Collection collection = new ArrayList();
                            collection.add(contextParams[pos]);
                            try {
                                ReflectionUtil.setFieldValue(object, field.getName(), collection);
                            } catch (Exception ignored) {
                            }
                        } else {
                            Object o = ObjectUtil.newObject(field.getType());
                            if (o != null && attributes != null) {
                                for (Field fieldOfField : o.getClass().getDeclaredFields()) {
                                    if (attributes.contains(fieldOfField.getName())) {
                                        try {
                                            Object mappingValue = fuzzObjectMapper.mapping(contextParams[pos], fieldOfField.getType());
                                            if (mappingValue != null && fieldOfField.getType().isAssignableFrom(mappingValue.getClass())) {
                                                ReflectionUtil.setFieldValue(o, fieldOfField.getName(), mappingValue);
                                            }
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                            }
                            try {
                                ReflectionUtil.setFieldValue(object, field.getName(), o);
                            } catch (Exception ignored) {
                            }
                        }
                    }
                } else {
                    // fix attributes forget situation
                    if (pos >= 0 && pos < contextParams.length && contextParams[pos] != null && attributes != null) {
                        for (Field fieldOfField : fieldValue.getClass().getDeclaredFields()) {
                            if (attributes.contains(field.getName())) {
                                try {
                                    Object mappingValue = fuzzObjectMapper.mapping(contextParams[pos], fieldOfField.getType());
                                    if (mappingValue != null && fieldOfField.getType().isAssignableFrom(mappingValue.getClass())) {
                                        ReflectionUtil.setFieldValue(fieldValue, fieldOfField.getName(), mappingValue);
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        }
                        try {
                            ReflectionUtil.setFieldValue(object, field.getName(), fieldValue);
                        } catch (Exception ignored) {
                        }
                    }
                }

            }
        }
    }

    protected void connectContext(Seed seed, Context context, FuzzAnalysis fuzzAnalysis, MethodCall targetMethodCall, MethodCall triggeredMethodCall) {
        // connect context
        if (triggeredMethodCall.isStatic() || triggeredMethodCall.isConstructor()) return;

        if (targetMethodCall.getClazz().equals(triggeredMethodCall.getClazz())) {
            // if belong to the same class
            Object contextObject = context.getObject();
            Object object = seed.getObject();

            Class<?> clazz = object.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                try {
                    Object fieldValue = ReflectionUtil.getFieldValue(contextObject, field.getName());
                    if (checkIsModified(fieldValue)) {
                        ReflectionUtil.setFieldValue(object, field.getName(), fieldValue);
                    }
                } catch (Throwable ignored) {
                }
            }
        } else {
            // if belong to different classes
            if (!targetMethodCall.isStatic()) {
                Object object = seed.getObject();

                assertNotNull(object);

                Class<?> clazz = object.getClass();
                for (Field field : clazz.getDeclaredFields()) {
                    if (fuzzAnalysis.isVariableSource(FieldUtil.getFieldSignature(clazz, field))) {
                        try {
                            if (field.getType().isAssignableFrom(context.getObject().getClass())) {
                                // directly set field
                                ReflectionUtil.setFieldValue(object, field.getName(), context.getObject());
                            } else if (List.class.isAssignableFrom(field.getType())) {
                                // warp with List
                                List list = (List) ObjectUtil.newObject(field.getType());
                                list.add(context.getObject());
                                ReflectionUtil.setFieldValue(object, field.getName(), list);
                            } else if (Set.class.isAssignableFrom(field.getType())) {
                                // warp with Set
                                Set set = (Set) ObjectUtil.newObject(field.getType());
                                set.add(context.getObject());
                                ReflectionUtil.setFieldValue(object, field.getName(), set);
                            }
                        } catch (Throwable ignored) {
                            log.warn("set variable field fail");
                        }
                    }
                }
                seed.setObject(object);
            }

            Object[] paramValues = seed.getParamValues();

            assertNotNull(paramValues);

            Class[] parameterTypes = targetMethodCall.getParameterTypes();

            assertEquals(parameterTypes.length, paramValues.length);

            for (int i = 0; i < paramValues.length; i++) {
                if (fuzzAnalysis.isVariableSource(targetMethodCall.getMethodSignature(), i)) {
                    try {
                        if (parameterTypes[i].isAssignableFrom(context.getObject().getClass())) {
                            // directly set field
                            paramValues[i] = context.getObject();
                        } else if (List.class.isAssignableFrom(parameterTypes[i])) {
                            // warp with List
                            List list = (List) ObjectUtil.newObject(parameterTypes[i]);
                            list.add(context.getObject());
                            paramValues[i] = list;
                        } else if (Set.class.isAssignableFrom(parameterTypes[i])) {
                            // warp with Set
                            Set set = (Set) ObjectUtil.newObject(parameterTypes[i]);
                            set.add(context.getObject());
                            paramValues[i] = set;
                        }
                    } catch (Throwable ignored) {
                        log.warn("set variable param fail");
                    }
                }
            }
            seed.setParamValues(paramValues);
        }
    }

    protected boolean checkIsModified(Object value) {
        if (value == null) {
            return false;
        }

        if (Integer.class.equals(value.getClass())) {
            return !((Integer) value).equals((int) 0);
        }

        if (Float.class.equals(value.getClass())) {
            return !((Float) value).equals((float) 0);
        }

        if (Character.class.equals(value.getClass())) {
            return !((Character) value).equals((char) 0);
        }

        if (Long.class.equals(value.getClass())) {
            return !((Long) value).equals((long) 0);
        }

        if (Byte.class.equals(value.getClass())) {
            return !((Byte) value).equals((byte) 0);
        }

        if (Short.class.equals(value.getClass())) {
            return !((Short) value).equals((short) 0);
        }

        if (Double.class.equals(value.getClass())) {
            return !((Double) value).equals((double) 0);
        }

        return true;
    }


    protected Pair<FuzzResult, Context> processTriggerSeed(long beginTime,
                                                           @NonNull MethodCall nextFuzzMethodCall,
                                                           @NonNull Seed triggerSeed,
                                                           @NonNull String gptQuery,
                                                           String gptAnswer,
                                                           @NonNull ValidateResult validateResult,
                                                           @NonNull InvokeMethodResult invokeMethodResult) {
        // process the result fuzzResult
        FuzzResult fuzzResult = new FuzzResult();
        fuzzResult.setSeed(triggerSeed);
        fuzzResult.setConsumeTime(System.currentTimeMillis() - beginTime);
        fuzzResult.setFuzzTargetObj(triggerSeed.getObject());
        fuzzResult.setFuzzArgs(triggerSeed.getParamValues());
        fuzzResult.setInvokeResult(invokeMethodResult);
        fuzzResult.setValidateThrow(validateResult.getValidateThrow());
        fuzzResult.setSuccess(validateResult.isSuccess());

        // process the new context
        Context newContext = new Context();
        newContext.setClazz(nextFuzzMethodCall.getClazz());
        newContext.setObject(triggerSeed.getObject());
        newContext.setParamsValue(triggerSeed.getParamValues());
        newContext.setGptQuery(gptQuery);
        newContext.setGptAnswer(gptAnswer);
        newContext.setMethodType(nextFuzzMethodCall.getMethodType());
        if (nextFuzzMethodCall.isMethod()) {
            newContext.setMethod(nextFuzzMethodCall.getMethod());
        } else if (nextFuzzMethodCall.isConstructor()) {
            newContext.setConstructor(nextFuzzMethodCall.getConstructor());
        } else {
            throw new RuntimeException("method type error");
        }

        return new Pair<>(fuzzResult, newContext);
    }

    private boolean checkInputValid(Object[] validInputs, Object[] inputParams) {
        if (validInputs.length != inputParams.length) return false;
        for (int i = 0; i < validInputs.length; i++) {
            if (validInputs[i] == null) {
                if (inputParams[i] != null) {
                    return false;
                }
            } else {
                if (validInputs[i] instanceof String || validInputs[i] instanceof URI) {
                    if (!Objects.equals(validInputs[i], inputParams[i])) {
                        return false;
                    }
                }
                if (validInputs[i].getClass().isPrimitive()) {
                    if (validInputs[i] != inputParams[i]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected ValidateResult validateInvokeResult(FuzzContext context, InvokeMethodResult result) throws Exception {

        TestcaseUnit testcaseUnit = context.getTestcaseUnit();
        Class<?> clazz = instrumentedFuzzLoader.loadClass(testcaseUnit.getTestcaseClassName());

        Method method;
        Object testInput;

        boolean returnValueValidateStatus = false;
        boolean throwValidateStatus = false;

        // check reachable
        if (ResultStatus.NotReached.equals(result.getResultStatus())) {
            return new ValidateResult(false);
        }

        // check input valid
        if (!checkInputValid(context.getValidInput(), result.getInputParams())) {
            return new ValidateResult(false);
        }

        // check timeout
        if (result.getThrowValue() instanceof TestTimedOutException) {
            return new ValidateResult(true, new TestTimedOutException(TIME_OUT, TIME_UNIT));
        }

        Throwable validateThrow = null;
        if (testcaseUnit.isNeedValidateReturnValue()) {
            method = ReflectionUtil.getMethodByName(clazz, testcaseUnit.getValidateReturnValueMethodName());

            assert method != null;

            testInput = result.getReturnValue();
            try {
                method.invoke(clazz.newInstance(), testInput);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof AssertionError) {
                    validateThrow = e.getCause();
                    returnValueValidateStatus = true;
                } else {
                    log.warn("ValidateFail: ", e.getCause());
                    return new ValidateResult(false);
                }
            }
        }

        if (testcaseUnit.isNeedValidateThrow()) {
            method = ReflectionUtil.getMethodByName(clazz, testcaseUnit.getValidateThrowMethodName());

            assert method != null;

            testInput = result.getThrowValue();
            try {
                method.invoke(clazz.newInstance(), testInput);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof AssertionError) {
                    validateThrow = e.getTargetException();
                    throwValidateStatus = true;
                } else {
                    log.warn("ValidateFail: ", e.getCause());
                    return new ValidateResult(false);
                }
            }
        }

        if (testcaseUnit.isNeedValidateThrow() && testcaseUnit.isNeedValidateReturnValue()) {
            return new ValidateResult(returnValueValidateStatus && throwValidateStatus, validateThrow);
        } else if (testcaseUnit.isNeedValidateReturnValue()) {
            return new ValidateResult(returnValueValidateStatus, validateThrow);
        } else if (testcaseUnit.isNeedValidateThrow()) {
            return new ValidateResult(throwValidateStatus, validateThrow);
        } else {
            log.error("An illegal verification status has occurred");
        }

        return new ValidateResult(false);
    }

    protected InvokeMethodResult invokeMethod(Seed seed, MethodCall targetMethodCall) {
        ClassLoader currnetClassLoader = Thread.currentThread().getContextClassLoader();

        Thread.currentThread().setContextClassLoader(instrumentedFuzzLoader);
        InvokeMethodResult invokeMethodResult = new InvokeMethodResult();
        try {
            beforeInvokeMethod(); // reset the state node map

            Throwable throwable = null;
            if (targetMethodCall.isMethod()) {
                Method fuzzMethod = targetMethodCall.getMethod();
                fuzzMethod.setAccessible(true);

                throwable = new MethodInvokeWrapper(
                        () -> fuzzMethod.invoke(seed.getObject(), seed.getParamValues()), TIME_OUT, TIME_UNIT)
                        .invoke();
            } else if (targetMethodCall.isConstructor()) {
                Constructor<?> fuzzConstructor = targetMethodCall.getConstructor();
                fuzzConstructor.setAccessible(true);

                throwable = new MethodInvokeWrapper(
                        () -> fuzzConstructor.newInstance(seed.getParamValues()), TIME_OUT, TIME_UNIT)
                        .invoke();
            } else {
                throw new RuntimeException("method type error");
            }

            // log if throwable error
            if (throwable instanceof NoClassDefFoundError) {
                log.error("fuzz constructor error: ", throwable);
            }

            afterInvokeMethod(invokeMethodResult);

            // process the timeout
            if (throwable instanceof TestTimedOutException) {
                invokeMethodResult.setThrowValue(throwable);
            }
        } catch (Exception t) {
            log.error("InvokeMethodError: ", t);
            return null;
        }

        Thread.currentThread().setContextClassLoader(currnetClassLoader);
        return invokeMethodResult;
    }

    private void beforeInvokeMethod() throws Exception {
        // reset the GlobalStateTable
        Class<?> stateTableClass = instrumentedFuzzLoader.loadClass(FuzzClassLoader.STATE_TABLE_CLASS_NAME);
        Method resetMethod = stateTableClass.getDeclaredMethod("reset");
        resetMethod.invoke(null);
    }

    private void afterInvokeMethod(InvokeMethodResult result) throws Exception {
        // retrieve the stateNode to InvokeMethodResult
        MethodCall vulMethodCall = this.chain.getVulMethodCall();
        String methodSignature = vulMethodCall.getMethodSignature();

        Class<?> tableClass = instrumentedFuzzLoader.loadClass(FuzzClassLoader.STATE_TABLE_CLASS_NAME);
        Class<?> nodeClass = instrumentedFuzzLoader.loadClass(FuzzClassLoader.STATE_NODE_CLASS_NAME);

        Method getStateSetMethod = tableClass.getDeclaredMethod("getStateSet");
        Method getStateTableMethod = tableClass.getDeclaredMethod("getStateTable");
        Method getValueMethod = nodeClass.getDeclaredMethod("getValue");

        Set<String> stateSet = (Set<String>) getStateSetMethod.invoke(null);
        if (stateSet.contains(methodSignature)) {
            result.setResultStatus(ResultStatus.Reached);
        } else {
            result.setResultStatus(ResultStatus.NotReached);
        }

        Map<String, StateNode> stateNodeMap = (Map<String, StateNode>) getStateTableMethod.invoke(null);

        String returnKey = methodSignature + "#return";
        String throwKey = methodSignature + "#throw";

        Object returnStateNode = stateNodeMap.getOrDefault(returnKey, null);
        if (returnStateNode != null) {
            Object returnValue = getValueMethod.invoke(returnStateNode);
            result.setReturnValue(returnValue);
        } else {
            result.setReturnValue(null);
        }

        Object throwStateNode = stateNodeMap.getOrDefault(throwKey, null);
        if (throwStateNode != null) {
            Object throwValue = getValueMethod.invoke(throwStateNode);
            result.setThrowValue((Throwable) throwValue);
        } else {
            result.setThrowValue(null);
        }

        Integer argNum = vulMethodCall.getArgNum();
        Object[] inputParams = new Object[argNum];
        for (int i = 0; i < argNum; i++) {
            String key = vulMethodCall.getMethodSignature() + "#" + i;
            Object stateNode = stateNodeMap.get(key);
            if (stateNode != null) {
                inputParams[i] = getValueMethod.invoke(stateNode);
            } else {
                log.warn("input state node is null");
            }
        }
        result.setInputParams(inputParams);
    }

    protected FuzzAnalysis analysisMethodCallStep(MethodCall targetMethodCall, MethodCall triggeredMethodCall) {
        Set<TaintObject> cfgTaintObjects = null, mpTaintObjects = null, varTaintObjects = null;

        // taint Control-Flow-Related variables
        TaintFlowAnalysis cfgAnalysis = new ControlFlowVariableAnalysis(targetMethodCall, triggeredMethodCall);
        cfgTaintObjects = cfgAnalysis.doAnalysis();
        log.info("ControlFlowVariableAnalysis: {}", cfgTaintObjects);

        // taint Exploit-Related variables
        TaintFlowAnalysis methodAnalysis = new MethodParameterAnalysis(targetMethodCall, triggeredMethodCall);
        mpTaintObjects = methodAnalysis.doAnalysis();
        log.info("MethodParameterAnalysis: {}", mpTaintObjects);

        // Map Exploit-Related variables
        MethodParameterMappingAnalysis mapAnalysis = new MethodParameterMappingAnalysis(targetMethodCall, triggeredMethodCall);
        mapAnalysis.doAnalysis();
        Map<Integer, Set<TaintObject>> taintObjectMap = mapAnalysis.getTaintObjectMap();
        log.info("MethodParameterMappingAnalysis: {}", taintObjectMap);

        if (!targetMethodCall.getClazz().equals(triggeredMethodCall.getClazz()) && !triggeredMethodCall.isStatic()) {
            TaintFlowAnalysis variableTrackingAnalysis = new VariableTrackingAnalysis(targetMethodCall, triggeredMethodCall);
            varTaintObjects = variableTrackingAnalysis.doAnalysis();
            log.info("VariableTrackingAnalysis: {}", varTaintObjects);
        }

        // remove the Exploit-Related variables taint in cfgTaint
        for (TaintObject mpTaintObject : mpTaintObjects) {
            cfgTaintObjects.remove(mpTaintObject);
        }

        if (varTaintObjects != null) {
            for (TaintObject varTaintObject : varTaintObjects) {
                cfgTaintObjects.remove(varTaintObject);
            }
        }

        return new FuzzAnalysis(cfgTaintObjects, mpTaintObjects, taintObjectMap, varTaintObjects);
    }

}
