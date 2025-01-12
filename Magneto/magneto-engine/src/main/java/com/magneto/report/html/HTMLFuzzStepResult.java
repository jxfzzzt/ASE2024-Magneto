package com.magneto.report.html;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HTMLFuzzStepResult {
    private String consumeTime;

    private List<Map<String, String>> argsMapList;

    private Map<String, String> fieldMap;

    private Map<String, String> invokeResultMap;
}
