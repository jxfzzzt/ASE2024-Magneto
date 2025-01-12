package com.magneto.report.html;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HTMLFuzzChainResultWrapper {

    private List<String> methodCallList;

    private Map<String, HTMLFuzzChainResult> fuzzResultMap; // cve name ---> fuzz chain result
}
