package com.magneto.fuzz.runner;

import com.magneto.fuzz.result.FuzzChainResultWrapper;
import com.magneto.staticanalysis.MethodCallChain;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class FuzzRunner {

    protected final static long TIME_OUT = 90000L;

    protected final static TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    protected FuzzRunner() {
    }

    public abstract FuzzChainResultWrapper fuzzCallChain(MethodCallChain chain) throws Exception;
}
