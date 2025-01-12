package com.magneto.fuzz.runner;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class CallableStatement implements Callable<Throwable> {

    private final CountDownLatch startLatch = new CountDownLatch(1);

    private final MethodInvokeTask task;

    public CallableStatement(MethodInvokeTask task) {
        this.task = task;
    }

    @Override
    public Throwable call() throws Exception {
        try {
            startLatch.countDown();
            task.invokeTask();
        } catch (Exception e) {
            throw e;
        } catch (Throwable e) {
            return e;
        }
        return null;
    }

    public void awaitStarted() throws InterruptedException {
        startLatch.await();
    }
}
