package com.magneto.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class TimeUtil {

    private final String name;

    private long elapsedTime = 0;

    private long startTime;

    private boolean inCounting = false;

    public TimeUtil(String name) {
        this.name = name;
    }

    public void start() {
        if (!inCounting) {
            inCounting = true;
            startTime = System.currentTimeMillis();
        }
    }

    public void stop() {
        if (inCounting) {
            elapsedTime += System.currentTimeMillis() - startTime;
            inCounting = false;
        }
    }

    public float inSecond() {
        return elapsedTime / 1000F;
    }

    public void clear() {
        elapsedTime = 0;
        inCounting = false;
    }

    @Override
    public String toString() {
        return String.format("[%s] elapsed time: %.2fs",
                name, inSecond());
    }

    /**
     * Runs a task, log the elapsed time, and return the result.
     *
     * @param task     task to be executed
     * @param taskName name of the task
     */
    public static <T> T runTask(Supplier<T> task, String taskName) {
        log.info("[{}] starts ...", taskName);
        TimeUtil timeUtil = new TimeUtil(taskName);
        timeUtil.start();
        T result = task.get();
        timeUtil.stop();
        log.info("[{}] finishes, elapsed time: {}", taskName,
                String.format("%.3fs", timeUtil.inSecond()));
        return result;
    }


    public static void runTask(Runnable task, String taskName) {
        runTask(() -> {
            task.run();
            return null;
        }, taskName);
    }

}
