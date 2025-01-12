package com.magneto.report.json;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JSONFuzzChainResult {

    @JSONField(name = "fuzzChainTime")
    private Long fuzzChainTime;

    @JSONField(name = "fuzzChainTimeStr")
    private String fuzzChainTimeStr;

    @JSONField(name = "successStep")
    private Integer successStep;

    @JSONField(name = "fuzzStepList")
    private List<JSONFuzzStepResult> fuzzStepList;
}
