package com.magneto.fuzz.runner;

@FunctionalInterface
public interface MethodInvokeTask {

    void invokeTask() throws Exception;

}
