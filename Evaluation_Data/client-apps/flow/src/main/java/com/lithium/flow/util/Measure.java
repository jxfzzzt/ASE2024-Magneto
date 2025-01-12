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

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Longs;

/**
 * @author Matt Ayres
 */
public class Measure {
	private final String name;
	private final Function<Number, String> printer;
	private final Multiset<Object> uniques = HashMultiset.create();
	private long[] bins = new long[1024];
	private long startTime;
	private long todo;
	private long done;
	private boolean forEta;
	private boolean hideAvg;

	public Measure(@Nonnull String name, @Nonnull Function<Number, String> printer) {
		this.name = checkNotNull(name);
		this.printer = checkNotNull(printer);
	}

	public boolean isForEta() {
		return forEta;
	}

	@Nonnull
	public Measure useForEta() {
		forEta = true;
		return this;
	}

	@Nonnull
	public Measure hideAvg() {
		hideAvg = true;
		return this;
	}

	public synchronized void add(@Nonnull Object object) {
		checkNotNull(object);
		uniques.add(object);
		done = uniques.elementSet().size();
	}

	public synchronized void remove(@Nonnull Object object) {
		checkNotNull(object);
		uniques.remove(object);
		done = uniques.elementSet().size();
	}

	public boolean isHideAvg() {
		return hideAvg;
	}

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public Function<Number, String> getPrinter() {
		return printer;
	}

	public void incTodo() {
		addTodo(1);
	}

	public void incSkip() {
		addSkip(1);
	}

	public void decTodo() {
		addTodo(-1);
	}

	public void subTodo(long value) {
		addTodo(-value);
	}

	public synchronized void addTodo(long value) {
		todo += value;
	}

	public void incDone() {
		addDone(1);
	}

	public void decDone() {
		addDone(-1);
	}

	public void subDone(long value) {
		addDone(-value);
	}

	public synchronized void addDone(long value) {
		if (startTime == 0) {
			startTime = System.currentTimeMillis();
		}

		int bin = getBin(System.currentTimeMillis());
		bins = Longs.ensureCapacity(bins, bin + 1, 1024);
		bins[bin] += value;
		done += value;
	}

	public synchronized void addSkip(long value) {
		todo += value;
		done += value;
	}

	public synchronized long getTodo() {
		return todo;
	}

	public synchronized long getDone() {
		return done;
	}

	public synchronized long getLeft() {
		return todo - done;
	}

	public synchronized double avg(long interval) {
		if (startTime == 0) {
			return 0;
		}

		long time = System.currentTimeMillis();
		int startBin = Math.max(0, getBin(time - interval));
		int endBin = getBin(time);
		if (startBin == endBin) {
			return 0;
		}

		double total = 0;
		double count = 0;
		for (int i = startBin; i < Math.min(endBin, bins.length - 1); i++) {
			total += bins[i];
			count++;
		}
		return total / count;
	}

	private int getBin(long time) {
		return (int) ((time - startTime) / 1000);
	}
}
