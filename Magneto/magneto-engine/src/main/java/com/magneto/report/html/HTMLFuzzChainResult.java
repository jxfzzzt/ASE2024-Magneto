package com.magneto.report.html;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HTMLFuzzChainResult {

    private Integer successStep;

    private List<HTMLFuzzStepResult> fuzzStepList;

}
