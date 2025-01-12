package com.magneto.report.html;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HTMLPage {

    private String projectPath;

    private List<String> vulDependencyList;

    private String totalTime;

    private List<HTMLFuzzChainResultWrapper> fuzzChainList;

    public HTMLPage() {

    }

    @Override
    public String toString() {
        return "HTMLPage{" +
                "projectPath='" + projectPath + '\'' +
                ", vulDependencyList=" + vulDependencyList +
                ", totalTime='" + totalTime + '\'' +
                ", fuzzItemList=" + fuzzChainList +
                '}';
    }
}

