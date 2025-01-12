package com.magneto.report.html;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HtmlUtil;
import com.magneto.config.GlobalConfiguration;
import com.magneto.config.ProjectContext;
import com.magneto.dependency.MavenDependency;
import com.magneto.fuzz.result.FuzzChainResult;
import com.magneto.fuzz.result.FuzzChainResultWrapper;
import com.magneto.fuzz.result.FuzzResult;
import com.magneto.report.ReportGenerator;
import com.magneto.staticanalysis.MethodCall;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class HTMLReportGenerator extends ReportGenerator {
    @Override
    public void generate(ProjectContext context, List<FuzzChainResultWrapper> fuzzChainResultList) throws Exception {
        // init
        Properties properties = new Properties();
        properties.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);

        // Create `Velocity` context
        VelocityContext velocityContext = new VelocityContext();

        // Set data
        velocityContext.put("title", "Fuzzing Report");

        HTMLPage htmlPage = getHtmlPage(context, fuzzChainResultList);
        velocityContext.put("context", htmlPage);

        // Retrieve template
        Template template = Velocity.getTemplate("report.vm", "utf-8");

        // Create output Writer
        FileWriter fileWriter = null;
        try {
            String outputPath = GlobalConfiguration.OUTPUT_DIR_PATH + File.separator + "report.html";
            fileWriter = new FileWriter(outputPath);

            // Render template
            template.merge(velocityContext, fileWriter);
        } finally {
            if (fileWriter != null) {
                fileWriter.flush();
                fileWriter.close();
            }
        }
    }

    private HTMLPage getHtmlPage(ProjectContext context, List<FuzzChainResultWrapper> fuzzChainResultWrappers) throws Exception {
        HTMLPage htmlPage = new HTMLPage();
        htmlPage.setProjectPath(context.getProjectPath());

        List<MavenDependency> vulDependency = context.getVulDependency();
        List<String> vulDependencyList = vulDependency.stream()
                .map(MavenDependency::getDescriptor)
                .map(HtmlUtil::escape)
                .collect(Collectors.toList());
        htmlPage.setVulDependencyList(vulDependencyList);

        String totalTime = DateUtil.formatBetween(context.getTotalTime(), BetweenFormatter.Level.SECOND);
        htmlPage.setTotalTime(totalTime);

        List<HTMLFuzzChainResultWrapper> htmlFuzzChainResultWrapperList = new ArrayList<>();
        for (FuzzChainResultWrapper fuzzChainResultWrapper : fuzzChainResultWrappers) {
            HTMLFuzzChainResultWrapper htmlFuzzChainResultWrapper = new HTMLFuzzChainResultWrapper();

            // process the method call chain
            List<String> methodCallList = new ArrayList<>();
            List<MethodCall> methodCalls = fuzzChainResultWrapper.getFuzzMethodCallChain().forwardChainList();
            for (int i = 0; i < methodCalls.size(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < i; j++) {
                    sb.append("&nbsp;&nbsp;&nbsp;");
                }
                sb.append(HtmlUtil.escape(methodCalls.get(i).getMethodSignature()));
                methodCallList.add(sb.toString());
            }
            htmlFuzzChainResultWrapper.setMethodCallList(methodCallList);

            // process the fuzz result map
            Map<String, FuzzChainResult> fuzzChainResultMap = fuzzChainResultWrapper.getFuzzChainResult();
            Map<String, HTMLFuzzChainResult> htmlFuzzChainResultMap = new HashMap<>();
            for (Map.Entry<String, FuzzChainResult> entry : fuzzChainResultMap.entrySet()) {
                String vulName = entry.getKey();
                FuzzChainResult fuzzChainResult = entry.getValue();

                HTMLFuzzChainResult htmlFuzzChainResult = new HTMLFuzzChainResult();

                htmlFuzzChainResult.setSuccessStep(fuzzChainResult.getSuccessStepNum());

                List<HTMLFuzzStepResult> htmlFuzzStepResultList = new ArrayList<>();
                for (FuzzResult fuzzResult : fuzzChainResult.getFuzzResultList()) {
                    HTMLFuzzStepResult htmlFuzzStepResult = new HTMLFuzzStepResult();

                    htmlFuzzStepResult.setConsumeTime(DateUtil.formatBetween(fuzzResult.getConsumeTime(), BetweenFormatter.Level.SECOND));

                    Object[] fuzzArgs = fuzzResult.getFuzzArgs();
                    List<Map<String, String>> argsMapList = new ArrayList<>();
                    for (int i = 0; i < fuzzArgs.length; i++) {
                        Map<String, String> fieldMap = getFieldMap(fuzzArgs[i]);
                        argsMapList.add(fieldMap);
                    }
                    htmlFuzzStepResult.setArgsMapList(argsMapList);

                    Object fuzzTargetObj = fuzzResult.getFuzzTargetObj();
                    if (fuzzTargetObj == null) {
                        htmlFuzzStepResult.setFieldMap(null);
                    } else {
                        Map<String, String> fieldMap = getFieldMap(fuzzTargetObj);
                        htmlFuzzStepResult.setFieldMap(fieldMap);
                    }

                    Map<String, String> invokeResultMap = getInvokeResultMap(fuzzResult);
                    htmlFuzzStepResult.setInvokeResultMap(invokeResultMap);

                    htmlFuzzStepResultList.add(htmlFuzzStepResult);
                }
                htmlFuzzChainResult.setFuzzStepList(htmlFuzzStepResultList);

                htmlFuzzChainResultMap.put(vulName, htmlFuzzChainResult);
            }

            htmlFuzzChainResultWrapper.setFuzzResultMap(htmlFuzzChainResultMap);

            htmlFuzzChainResultWrapperList.add(htmlFuzzChainResultWrapper);
        }

        htmlPage.setFuzzChainList(htmlFuzzChainResultWrapperList);

        return htmlPage;

    }

}
