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
import static com.lithium.flow.util.PrintUtils.time;

import com.lithium.flow.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class Progress implements AutoCloseable {
	private static final Logger log = Logs.getLogger();

	private final List<Measure> measures = new ArrayList<>();
	private Thread loop;
	private long startTime;
	private boolean stopped;

	@Nonnull
	public Measure measure(@Nonnull String name) {
		return measure(name, n -> String.valueOf(n.longValue()));
	}

	@Nonnull
	public Measure counter(@Nonnull String name) {
		return measure(name, PrintUtils::count);
	}

	@Nonnull
	public Measure bandwidth(@Nonnull String name) {
		return measure(name, PrintUtils::bytes);
	}

	@Nonnull
	public Measure unique(@Nonnull String name) {
		return measure(name).hideAvg();
	}

	@Nonnull
	public synchronized Measure measure(@Nonnull String name, @Nonnull Function<Number, String> printer) {
		checkNotNull(name);
		checkNotNull(printer);

		Measure measure = new Measure(name, printer);
		measures.add(measure);
		return measure;
	}

	@Nonnull
	public synchronized Progress start(long logInterval, long avgInterval) {
		if (loop != null) {
			throw new IllegalStateException("already started");
		} else if (stopped) {
			throw new IllegalStateException("already used");
		}

		startTime = System.currentTimeMillis();
		loop = new LoopThread(logInterval, () -> log(avgInterval));
		return this;
	}

	public synchronized void stop() {
		if (loop == null) {
			throw new IllegalStateException("not started");
		} else if (stopped) {
			throw new IllegalStateException("already used");
		}

		loop.interrupt();
		stopped = true;
	}

	/**
	 * @deprecated Use {@link #close()} instead.
	 */
	@Deprecated
	public void finish() {
		close();
	}

	@Override
	public void close() {
		stop();
		log(System.currentTimeMillis() - startTime);
	}

	public synchronized void log(long avgInterval) {
		long elapsed = System.currentTimeMillis() - startTime;
		if (!stopped && elapsed < 1000) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		if (startTime > 0) {
			sb.append(time(elapsed));
		}

		if (stopped) {
			sb.append(" (done)");
		} else {
			double eta = avgInterval > 0 ? getEta(avgInterval) : 0;
			if (eta > 0) {
				sb.append(" (").append(time(eta)).append(" eta)");
			}
		}

		for (Measure measure : measures) {
			Function<Number, String> printer = measure.getPrinter();
			long todo = measure.getTodo();
			long done = measure.getDone();

			sb.append(", ").append(printer.apply(done));
			if (todo > 0) {
				sb.append("/").append(printer.apply(todo));
			}
			sb.append(" ").append(measure.getName());
			if (!measure.isHideAvg()) {
				sb.append(" (").append(printer.apply(measure.avg(avgInterval)));
				sb.append("/s)");
			}
		}

		log.info(sb.toString());
	}

	private double getEta(long avgInterval) {
		return measures.stream()
				.filter(Measure::isForEta)
				.filter(m -> m.avg(avgInterval) > 0)
				.mapToDouble(m -> m.getLeft() * 1000 / m.avg(avgInterval))
				.max().orElse(0);
	}

	@Nonnull
	public static Progress start(@Nonnull Config config) {
		return new Progress().start(config.getTime("log.interval", "5s"), config.getTime("avg.interval", "1m"));
	}
}
