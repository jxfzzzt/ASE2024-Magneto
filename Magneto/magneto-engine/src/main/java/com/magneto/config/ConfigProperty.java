package com.magneto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "config")
public class ConfigProperty {

    private String groundTruthPath = System.getProperty("user.dir") + File.separator + "groundtruth";

    private Integer maxCallChainLength = 10;

    private Integer maxGptRequestLimit = 5;

    private String outputDir = "output";

    private Integer maxPromptScope = 2;

    private Integer maxInterAnalysisScope = 2;

    private Integer maxFuzzChainNumber = 15;

    private String gptModelName = "gpt-4";

    private Long maxFuzzTime = 600000L;

    private Integer maxRetryCount = 3;

    private Boolean enableGpt = true;

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public Integer getMaxInterAnalysisScope() {
        return maxInterAnalysisScope;
    }

    public void setMaxInterAnalysisScope(Integer maxInterAnalysisScope) {
        this.maxInterAnalysisScope = maxInterAnalysisScope;
    }

    public Integer getMaxPromptScope() {
        return maxPromptScope;
    }

    public void setMaxPromptScope(Integer maxPromptScope) {
        this.maxPromptScope = maxPromptScope;
    }

    public Integer getMaxFuzzChainNumber() {
        return maxFuzzChainNumber;
    }

    public void setMaxFuzzChainNumber(Integer maxFuzzChainNumber) {
        this.maxFuzzChainNumber = maxFuzzChainNumber;
    }

    public Long getMaxFuzzTime() {
        return maxFuzzTime;
    }

    public void setMaxFuzzTime(Long maxFuzzTime) {
        this.maxFuzzTime = maxFuzzTime;
    }

    public String getGroundTruthPath() {
        return groundTruthPath;
    }

    public void setGroundTruthPath(String groundTruthPath) {
        this.groundTruthPath = groundTruthPath;
    }

    public Integer getMaxCallChainLength() {
        return maxCallChainLength;
    }

    public void setMaxCallChainLength(Integer maxCallChainLength) {
        this.maxCallChainLength = maxCallChainLength;
    }

    public void setGptModelName(String gptModelName) {
        this.gptModelName = gptModelName;
    }

    public String getGptModelName() {
        return gptModelName;
    }

    public Integer getMaxGptRequestLimit() {
        return maxGptRequestLimit;
    }

    public void setMaxGptRequestLimit(Integer maxGptRequestLimit) {
        this.maxGptRequestLimit = maxGptRequestLimit;
    }

    public Integer getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(Integer maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public void setEnableGpt(Boolean enableGpt) {
        this.enableGpt = enableGpt;
    }

    public Boolean getEnableGpt() {
        return enableGpt;
    }
}
