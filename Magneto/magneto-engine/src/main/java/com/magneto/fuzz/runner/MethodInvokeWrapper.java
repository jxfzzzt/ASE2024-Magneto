package com.magneto.fuzz.runner;

import org.junit.runners.model.TestTimedOutException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MethodInvokeWrapper {

    private final MethodInvokeTask invokeTask;

    private final Long timeout;

    private final TimeUnit timeUnit;

    public MethodInvokeWrapper(MethodInvokeTask invokeTask, long timeout, TimeUnit timeUnit) {
        this.invokeTask = invokeTask;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public Throwable invoke() {
        CallableStatement callable = new CallableStatement(invokeTask);

        FutureTask<Throwable> task = new FutureTask<>(callable);

        Thread thread = new Thread(task, "run-test-method-thread");
        thread.setDaemon(true);
        thread.start();

        try {
            callable.awaitStarted();
        } catch (InterruptedException ignored) {
        }

        return getResult(thread, task);
    }

    private Throwable getResult(Thread thread, FutureTask<Throwable> task) {
        try {
            if (timeout > 0) {
                return task.get(timeout, timeUnit);
            } else {
                return task.get();
            }
        } catch (InterruptedException e) {
            return e;
        } catch (ExecutionException e) {
            return e.getCause();
        } catch (TimeoutException e) {
            return createTimeoutException(timeout, thread);
        }
    }

    private Exception createTimeoutException(long timeout, Thread thread) {
        StackTraceElement[] stackTrace = thread.getStackTrace();
        Exception currThreadException = new TestTimedOutException(timeout, timeUnit);
        currThreadException.setStackTrace(stackTrace);
        thread.interrupt();
        return currThreadException;
    }
}
