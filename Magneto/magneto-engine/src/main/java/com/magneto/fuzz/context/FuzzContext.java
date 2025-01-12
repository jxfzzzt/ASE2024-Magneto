package com.magneto.fuzz.context;

import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import com.magneto.config.ClientProjectProperty;
import com.magneto.fuzz.FuzzClassLoader;
import com.magneto.instrument.state.StateNode;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;
import com.magneto.testcase.TestCaseService;
import com.magneto.testcase.model.TestcaseUnit;
import com.magneto.testcase.runner.RunResult;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class FuzzContext {

    private static final TestCaseService TEST_CASE_SERVICE = SpringUtil.getBean(TestCaseService.class);

    private final FuzzClassLoader fuzzClassLoader;

    private RunResult testcaseRunResult;

    private final Stack<Context> contextStack;

    private final TestcaseUnit testcaseUnit;

    private final MethodCallChain methodCallChain;

    private Object[] validInput;

    public FuzzContext(@NonNull FuzzClassLoader fuzzClassLoader, @NonNull MethodCallChain methodCallChain, @NonNull TestcaseUnit testcaseUnit) {
        this.fuzzClassLoader = fuzzClassLoader;
        this.contextStack = new Stack<>();
        this.methodCallChain = methodCallChain;
        this.testcaseUnit = testcaseUnit;
        try {
            initialize();
        } catch (Exception e) {
            throw new RuntimeException("fuzz context initialize error.", e);
        }
    }

    private void initialize() throws Exception {
        String groupId = testcaseUnit.getGroupId();
        String artifactId = testcaseUnit.getArtifactId();
        String version = ClientProjectProperty.getDependencyVersionMap().getOrDefault(new Pair<>(groupId, artifactId), null);

        assert version != null;

        RunResult runResult = TEST_CASE_SERVICE.getTestcaseExecuteResult(fuzzClassLoader, testcaseUnit, version);
        this.testcaseRunResult = runResult;

        Context context = getContext(runResult);
        this.contextStack.push(context);
    }

    private Context getContext(RunResult runResult) {
        MethodCall vulMethodCall = methodCallChain.getVulMethodCall();

        Map<String, StateNode> runStateTable = runResult.getRunStateTable();

        Context context = new Context();

        Object[] paramsValue = new Object[vulMethodCall.getArgNum()];
        this.validInput = new Object[vulMethodCall.getArgNum()];

        for (int i = 0; i < vulMethodCall.getArgNum(); i++) {
            String key = vulMethodCall.getMethodSignature() + "#" + i;
            StateNode stateNode = runStateTable.get(key);

            assertNotNull(stateNode);

            paramsValue[i] = stateNode.getValue();
            validInput[i] = stateNode.getValue();
        }

        context.setParamsValue(paramsValue);
        context.setClazz(vulMethodCall.getClazz());
        context.setMethodType(vulMethodCall.getMethodType());

        if (vulMethodCall.isMethod()) {
            context.setMethod(vulMethodCall.getMethod());
        } else if (vulMethodCall.isConstructor()) {
            context.setConstructor(vulMethodCall.getConstructor());
        } else {
            throw new RuntimeException("method type error");
        }

        if (vulMethodCall.isStatic()) {
            context.setObject(null);
        } else {
            StateNode stateNode = runStateTable.getOrDefault(vulMethodCall.getMethodSignature() + "#this", null);
            if (stateNode != null) {
                context.setObject(stateNode.getValue());
            }
        }
        return context;
    }

    public void addNewContext(Context context) {
        this.contextStack.push(context);
    }

    public Context getLatestContext() {
        assert !contextStack.isEmpty();
        return this.contextStack.peek();
    }

    public RunResult getTestcaseRunResult() {
        return testcaseRunResult;
    }

    public TestcaseUnit getTestcaseUnit() {
        return testcaseUnit;
    }

    public Object[] getValidInput() {
        return validInput;
    }
}
