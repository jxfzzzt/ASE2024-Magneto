package com.magneto.report.json;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import com.alibaba.fastjson2.JSON;
import com.magneto.config.GlobalConfiguration;
import com.magneto.config.ProjectContext;
import com.magneto.fuzz.result.FuzzChainResult;
import com.magneto.fuzz.result.FuzzChainResultWrapper;
import com.magneto.fuzz.result.FuzzResult;
import com.magneto.report.ReportGenerator;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodCallChain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONReportGenerator extends ReportGenerator {
    @Override
    public void generate(ProjectContext context, List<FuzzChainResultWrapper> fuzzChainResultList) {
        JSONReport jsonReport = getJSONReport(context, fuzzChainResultList);

        String jsonString = JSON.toJSONString(jsonReport);

        String outputPath = GlobalConfiguration.OUTPUT_DIR_PATH + File.separator + "report.json";
        FileWriter writer = new FileWriter(outputPath);
        writer.write(jsonString);
    }

    private JSONReport getJSONReport(ProjectContext context, List<FuzzChainResultWrapper> fuzzChainResultWrappers) {
        JSONReport report = new JSONReport();
        report.setProjectPath(context.getProjectPath());

        int vulDependencyChainNum = context.getVulDependencyFinder().getVulDependencyChainList().size();
        report.setVulDependencyChainNum(vulDependencyChainNum);

        List<MethodCallChain> puringVulCallChainList = context.getVulCallChainFinder().getPuringVulCallChainList();
        int vulMethodCallChainNum = puringVulCallChainList.size();
        report.setVulMethodCallChainNum(vulMethodCallChainNum);
        int totalLength = 0;
        for (MethodCallChain methodCallChain : puringVulCallChainList) {
            totalLength += methodCallChain.length();
        }
        report.setAverageMethodCallChainLength(totalLength * 1.0 / vulMethodCallChainNum);

        report.setAnalysisTime(context.getAnalysisTime());
        report.setAnalysisTimeStr(DateUtil.formatBetween(context.getAnalysisTime(), BetweenFormatter.Level.SECOND));

        report.setFuzzTime(context.getFuzzTime());
        report.setFuzzTimeStr(DateUtil.formatBetween(context.getFuzzTime(), BetweenFormatter.Level.SECOND));

        report.setTotalTime(context.getTotalTime());
        report.setTotalTimeStr(DateUtil.formatBetween(context.getTotalTime(), BetweenFormatter.Level.SECOND));

        int totalSuccessStep = 0;
        int totalChainNum = 0;
        List<JSONFuzzChainResultWrapper> fuzzChainResultWrapperList = new ArrayList<>();
        for (FuzzChainResultWrapper fuzzChainResultWrapper : fuzzChainResultWrappers) {
            Map<String, FuzzChainResult> fuzzChainResultMap = fuzzChainResultWrapper.getFuzzChainResult();

            // process the method call chain
            MethodCallChain fuzzMethodCallChain = fuzzChainResultWrapper.getFuzzMethodCallChain();
            List<String> methodCallList = fuzzMethodCallChain.forwardChainList().stream()
                    .map(MethodCall::getMethodSignature)
                    .collect(Collectors.toList());

            JSONFuzzChainResultWrapper jsonFuzzChainResultWrapper = new JSONFuzzChainResultWrapper();
            jsonFuzzChainResultWrapper.setMethodCallList(methodCallList);

            // process fuzz chain result
            Map<String, JSONFuzzChainResult> jsonFuzzChainResultMap = new HashMap<>();
            for (Map.Entry<String, FuzzChainResult> entry : fuzzChainResultMap.entrySet()) {
                String vulName = entry.getKey();
                FuzzChainResult fuzzChainResult = entry.getValue();

                Integer successStepNum = fuzzChainResult.getSuccessStepNum();
                totalSuccessStep += successStepNum;
                totalChainNum++;

                JSONFuzzChainResult jsonFuzzChainResult = new JSONFuzzChainResult();
                jsonFuzzChainResult.setSuccessStep(successStepNum);

                jsonFuzzChainResult.setFuzzChainTime(fuzzChainResult.getFuzzChainTime());
                jsonFuzzChainResult.setFuzzChainTimeStr(DateUtil.formatBetween(fuzzChainResult.getFuzzChainTime(), BetweenFormatter.Level.SECOND));

                List<JSONFuzzStepResult> jsonFuzzStepResultList = new ArrayList<>();
                // process each step
                for (FuzzResult fuzzResult : fuzzChainResult.getFuzzResultList()) {
                    JSONFuzzStepResult jsonFuzzStepResult = new JSONFuzzStepResult();

                    jsonFuzzStepResult.setConsumeTime(fuzzResult.getConsumeTime());
                    jsonFuzzStepResult.setConsumeTimeStr(DateUtil.formatBetween(fuzzResult.getConsumeTime(), BetweenFormatter.Level.SECOND));

                    Object[] fuzzArgs = fuzzResult.getFuzzArgs();
                    List<Map<String, String>> argsMapList = new ArrayList<>();
                    for (int i = 0; i < fuzzArgs.length; i++) {
                        Map<String, String> fieldMap = getFieldMap(fuzzArgs[i]);
                        argsMapList.add(fieldMap);
                    }
                    jsonFuzzStepResult.setArgsMapList(argsMapList);

                    Object fuzzTargetObj = fuzzResult.getFuzzTargetObj();
                    if (fuzzTargetObj == null) {
                        jsonFuzzStepResult.setFieldMap(null);
                    } else {
                        Map<String, String> fieldMap = getFieldMap(fuzzTargetObj);
                        jsonFuzzStepResult.setFieldMap(fieldMap);
                    }

                    Map<String, String> invokeResultMap = getInvokeResultMap(fuzzResult);
                    jsonFuzzStepResult.setInvokeResultMap(invokeResultMap);

                    jsonFuzzStepResultList.add(jsonFuzzStepResult);
                }

                jsonFuzzChainResult.setFuzzStepList(jsonFuzzStepResultList);

                jsonFuzzChainResultMap.put(vulName, jsonFuzzChainResult);
            }

            jsonFuzzChainResultWrapper.setFuzzResultMap(jsonFuzzChainResultMap);

            fuzzChainResultWrapperList.add(jsonFuzzChainResultWrapper);
        }

        report.setFuzzChainList(fuzzChainResultWrapperList);
        report.setTotalSuccessStep(totalSuccessStep);
        report.setAverageSuccessStep(totalSuccessStep * 1.0 / totalChainNum);
        return report;
    }
}

