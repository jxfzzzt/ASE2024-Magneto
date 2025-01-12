package com.magneto.report.json;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JSONFuzzChainResultWrapper {
    @JSONField(name = "methodCallList")
    private List<String> methodCallList;

    @JSONField(name = "fuzzResultMap")
    private Map<String, JSONFuzzChainResult> fuzzResultMap; // cve name ---> fuzz chain result

}
