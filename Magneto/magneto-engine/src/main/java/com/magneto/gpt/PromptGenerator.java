package com.magneto.gpt;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson2.JSONObject;
import com.magneto.asm.traversal.ClassComponentSearcher;
import com.magneto.asm.traversal.FieldComponent;
import com.magneto.asm.traversal.MethodComponent;
import com.magneto.ast.ClassPuringWrapper;
import com.magneto.ast.MethodTrimming;
import com.magneto.config.GlobalConfiguration;
import com.magneto.dependency.MavenDependency;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.util.IOUtil;
import soot.Type;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

public class PromptGenerator {
    private static final String ANALYSIS_CODE_TEMPLATE = "analysis_code_template.txt";
    private final Map<PromptQueryRecord, String> PROMPT_RECORD_MAP = new HashMap<>();
    private final MethodCall targetMethodCall;
    private final MethodCall triggeredMethodCall;

    public PromptGenerator(MethodCall targetMethodCall, MethodCall triggeredMethodCall) {
        this.targetMethodCall = targetMethodCall;
        this.triggeredMethodCall = triggeredMethodCall;
    }

    public String generate() throws IOException {
        return generate(null, true);
    }

    public String generate(boolean needText) throws IOException {
        return generate(null, needText);
    }

    public String generate(StringBuilder abstractMethodPrompt) throws IOException {
        return generate(abstractMethodPrompt, true);
    }

    public String generate(StringBuilder abstractMethodPrompt, boolean needText) throws IOException {
        PromptQueryRecord record = new PromptQueryRecord(targetMethodCall, triggeredMethodCall);
        if (PROMPT_RECORD_MAP.containsKey(record)) {
            return PROMPT_RECORD_MAP.get(record);
        }

        Map<String, String> codeContent = getCodeContent();
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : codeContent.entrySet()) {
            String className = entry.getKey();
            // skip inner class to avoid duplicate code in prompt
            if (className.contains("$")) {
                continue;
            }

            String sourceCode = entry.getValue();
            sb.append("```java\n");
            sb.append(sourceCode);
            sb.append("```\n");
        }

        if (abstractMethodPrompt != null && abstractMethodPrompt.length() > 0) {
            sb.append(abstractMethodPrompt);
        }

        if (needText) {
            String textContent = getTextContent();
            sb.append(textContent);
        }

        String prompt = sb.toString();
        PROMPT_RECORD_MAP.put(record, prompt);
        return prompt;
    }

    private String getTextContent() throws IOException {
        InputStream inputStream = new ClassPathResource(ANALYSIS_CODE_TEMPLATE).getStream();
        String template = IOUtil.readString(inputStream);
        String targetMethodName = targetMethodCall.getMethodName();
        List<String> targetMethodArgTypes = targetMethodCall.getSootMethod().getParameterTypes().stream().map(Type::toQuotedString).collect(Collectors.toList());
        String triggeredMethodName = triggeredMethodCall.getMethodName();
        String targetMethodClassName = targetMethodCall.getClazz().getName();
        JSONObject jsonObject = new JSONObject();
        List<Type> triggeredMethodParameterTypes = triggeredMethodCall.getSootMethod().getParameterTypes();
        List<String> symbols = new ArrayList<>();

        for (int i = 0; i < triggeredMethodParameterTypes.size(); i++) {
            Type type = triggeredMethodParameterTypes.get(i);
            Map<String, String> value = new HashMap<>();
            value.put("type", type.toQuotedString());
            String symbol = String.format("<%s>", i);
            value.put("value", symbol);
            symbols.add(symbol);
            jsonObject.put(String.valueOf(i), value);
        }

        return String.format(template, targetMethodName, targetMethodArgTypes, triggeredMethodName,
                targetMethodName, triggeredMethodName, jsonObject, symbols, triggeredMethodName, targetMethodClassName, targetMethodName, targetMethodName);
    }

    // class name ---> class source code
    private Map<String, String> getCodeContent() throws IOException {
        // search part
        ClassComponentSearcher searcher = new ClassComponentSearcher(this.targetMethodCall, this.triggeredMethodCall);
        searcher.doSearch();
        searcher.validate();

        // ast process part
        Map<String, String> sourceCodeMap = new HashMap<>();
        Map<String, Set<MethodComponent>> methodComponentMap = searcher.getMethodComponentMap();
        Map<String, Set<FieldComponent>> fieldComponentMap = searcher.getFieldComponentMap();
        Set<String> classKeySet = new HashSet<>();
        classKeySet.addAll(methodComponentMap.keySet());
        classKeySet.addAll(fieldComponentMap.keySet());

        for (String className : classKeySet) {
            MavenDependency dependency = targetMethodCall.getDependency();

            // get sourceCode
            String filePath = className.replaceAll("\\.", "/");
            // process the inner class
            int index = filePath.lastIndexOf('$');
            if (index != -1) {
                filePath = filePath.substring(0, index);
            }
            filePath += ".java";

            File file = FileUtil.file(GlobalConfiguration.DECOMPILE_DIR.getAbsolutePath(), dependency.getDescriptor(), filePath);
            String sourceCode = new FileReader(file).readString();

            assertNotNull(sourceCode);

            ClassPuringWrapper puringWrapper = new ClassPuringWrapper(sourceCode,
                    fieldComponentMap.getOrDefault(className, new HashSet<>()),
                    methodComponentMap.getOrDefault(className, new HashSet<>()));

            sourceCodeMap.put(className, puringWrapper.getPuringResult());
        }

        trimCodeContent(sourceCodeMap);

        return sourceCodeMap;
    }

    private void trimCodeContent(Map<String, String> codeContent) {
        String className = targetMethodCall.getClazz().getName();
        String sourceCode = codeContent.getOrDefault(className, null);
        if (sourceCode == null) {
            codeContent.remove(className);
        } else {
            sourceCode = new MethodTrimming(sourceCode, targetMethodCall, triggeredMethodCall).getTrimmingResult();
            codeContent.put(className, sourceCode);
        }
    }

}
