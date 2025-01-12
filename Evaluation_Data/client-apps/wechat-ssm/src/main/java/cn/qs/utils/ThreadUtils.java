package cn.qs.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

	private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	public static void execute(Runnable runnable) {
		fixedThreadPool.execute(runnable);
	}

}
