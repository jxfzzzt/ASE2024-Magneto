package com.magneto.report.json;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JSONReport {

    @JSONField(name = "analysisTime")
    private Long analysisTime;

    @JSONField(name = "fuzzTime")
    private Long fuzzTime;

    @JSONField(name = "totalTime")
    private Long totalTime;

    @JSONField(name = "analysisTimeStr")
    private String analysisTimeStr;

    @JSONField(name = "fuzzTimeStr")
    private String fuzzTimeStr;

    @JSONField(name = "totalTimeStr")
    private String totalTimeStr;

    @JSONField(name = "projectPath")
    private String projectPath;

    @JSONField(name = "vulDependencyChainNum")
    private Integer vulDependencyChainNum;

    @JSONField(name = "vulMethodCallChainNum")
    private Integer vulMethodCallChainNum;

    @JSONField(name = "totalSuccessStep")
    private Integer totalSuccessStep;

    @JSONField(name = "averageSuccessStep")
    private Double averageSuccessStep;

    @JSONField(name = "averageMethodCallChainLength")
    private Double averageMethodCallChainLength;

    @JSONField(name = "fuzzChainList")
    private List<JSONFuzzChainResultWrapper> fuzzChainList;

}
