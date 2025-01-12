/*
 * Copyright 2015 Lithium Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lithium.flow.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.exception.IllegalConfigException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author Matt Ayres
 */
public class Threader implements AutoCloseable {
	private static final Logger log = Logs.getLogger();

	public static final int DEFAULT_CODE_THREADS = -1;
	public static final String DEFAULT_THREADS = "100%";
	public static final int DEFAULT_RETRIES = 0;
	public static final int DEFAULT_MAX_QUEUED = Integer.MAX_VALUE;
	public static final int DEFAULT_NEEDLE_PERMITS = Integer.MAX_VALUE;
	public static final boolean DEFAULT_DAEMON = false;

	private final ListeningExecutorService service;
	private final AtomicInteger remaining = new AtomicInteger();
	private final AtomicInteger queued = new AtomicInteger();
	private volatile int retries = DEFAULT_RETRIES;
	private volatile int maxQueued = DEFAULT_MAX_QUEUED;
	private volatile int needlePermits = DEFAULT_NEEDLE_PERMITS;

	public Threader() {
		this(DEFAULT_CODE_THREADS);
	}

	public Threader(int threads) {
		this(buildService(threads, false));
	}

	public Threader(@Nonnull ExecutorService service) {
		this.service = MoreExecutors.listeningDecorator(checkNotNull(service));
	}

	public int getRetries() {
		return retries;
	}

	@Nonnull
	public Threader setRetries(int retries) {
		this.retries = retries;
		return this;
	}

	@Nonnull
	public Threader withRetries(int retries) {
		this.retries = retries;
		return this;
	}

	public int getMaxQueued() {
		return maxQueued;
	}

	@Nonnull
	public Threader setMaxQueued(int maxQueued) {
		this.maxQueued = maxQueued;
		return this;
	}

	@Nonnull
	public Threader withMaxQueued(int maxQueued) {
		this.maxQueued = maxQueued;
		return this;
	}

	public int getNeedlePermits() {
		return needlePermits;
	}

	@Nonnull
	public Threader setNeedlePermits(int needlePermits) {
		this.needlePermits = needlePermits;
		return this;
	}

	@Nonnull
	public Threader withNeedlePermits(int needlePermits) {
		this.needlePermits = needlePermits;
		return this;
	}

	@Nonnull
	public ListenableFuture<Void> execute(@Nonnull String name, @Nonnull Executable executable) {
		return submit(name, executable);
	}

	@Nonnull
	public <T> ListenableFuture<T> submit(@Nonnull String name, @Nonnull Callable<T> callable) {
		return submit(name, callable, retries);
	}

	@Nonnull
	private <T> ListenableFuture<T> submit(@Nonnull String name, @Nonnull Callable<T> callable, int retriesLeft) {
		checkNotNull(name);
		checkNotNull(callable);

		if (retriesLeft == retries) {
			Sleep.until(() -> queued.get() < maxQueued);
		}

		remaining.incrementAndGet();
		queued.incrementAndGet();

		ListenableFuture<T> future = service.submit(() -> {
			queued.decrementAndGet();
			return callable.call();
		});

		Futures.addCallback(future, new FutureCallback<T>() {
			@Override
			public void onSuccess(T object) {
				remaining.decrementAndGet();
				log.debug("execution finished: {}", name);
			}

			@Override
			public void onFailure(@Nonnull Throwable throwable) {
				if (retriesLeft > 0) {
					submit(name, callable, retriesLeft - 1);
				}

				remaining.decrementAndGet();
				log.warn("execution failed: {} ({} retries left)", name, retriesLeft, throwable);
			}
		}, MoreExecutors.directExecutor());

		return future;
	}

	/**
	 * @deprecated Use {@link #close()} instead.
	 */
	@Deprecated
	public void finish() {
		close();
	}

	/**
	 * @deprecated Use {@link #close(long)} instead.
	 */
	@Deprecated
	public void finish(long timeout) {
		close(timeout);
	}

	@Override
	public void close() {
		close(-1);
	}

	public boolean close(long timeout) {
		long endTime = timeout == -1 ? Long.MAX_VALUE : System.currentTimeMillis() + timeout;
		Sleep.until(() -> remaining.get() == 0 || System.currentTimeMillis() >= endTime);
		service.shutdown();
		return remaining.get() == 0;
	}

	public int getRemaining() {
		return remaining.get();
	}

	public int getQueued() {
		return queued.get();
	}

	@Nonnull
	public <T> Needle<T> needle() {
		return new Needle<>(this, needlePermits);
	}

	@Nonnull
	public <T> Needle<T> needle(int permits) {
		return new Needle<>(this, permits);
	}

	@Nonnull
	public static Threader forCompute() {
		return new Threader(Runtime.getRuntime().availableProcessors());
	}

	@Nonnull
	public static Threader forDaemon() {
		return forDaemon(DEFAULT_CODE_THREADS);
	}

	@Nonnull
	public static Threader forDaemon(int threads) {
		return new Threader(buildService(threads, true));
	}

	@Nonnull
	public static Threader build(@Nonnull Config config) {
		int threads = getThreads(config);
		int retries = config.getInt("retries", DEFAULT_RETRIES);
		int maxQueued = config.getInt("maxQueued", DEFAULT_MAX_QUEUED);
		int needlePermits = config.getInt("needlePermits", DEFAULT_NEEDLE_PERMITS);
		boolean daemon = config.getBoolean("daemon", DEFAULT_DAEMON);

		return new Threader(buildService(threads, daemon))
				.withRetries(retries)
				.withMaxQueued(maxQueued)
				.withNeedlePermits(needlePermits);
	}

	public static int getThreads(@Nonnull Config config) {
		String value = config.getString("threads", DEFAULT_THREADS);
		try {
			if (value.endsWith("%")) {
				int percent = Integer.parseInt(value.substring(0, value.length() - 1));
				return Runtime.getRuntime().availableProcessors() * percent / 100;
			} else {
				return Integer.parseInt(value);
			}
		} catch (NumberFormatException e) {
			throw new IllegalConfigException("threads", value, "int", e);
		}
	}

	@Nonnull
	private static ExecutorService buildService(int threads, boolean daemon) {
		ThreadFactory defaultFactory = Executors.defaultThreadFactory();

		ThreadFactory daemonFactory = runnable -> {
			Thread thread = defaultFactory.newThread(runnable);
			thread.setDaemon(daemon);
			return thread;
		};

		return threads == -1
				? Executors.newCachedThreadPool(daemonFactory)
				: Executors.newFixedThreadPool(threads, daemonFactory);
	}
}
