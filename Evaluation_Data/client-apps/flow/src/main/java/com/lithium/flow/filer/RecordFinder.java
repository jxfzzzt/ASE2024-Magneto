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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.util.IndefiniteSpliterator;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Sleep;
import com.lithium.flow.util.Threader;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class RecordFinder extends IndefiniteSpliterator<Record> {
	private static final Logger log = Logs.getLogger();

	private final Filer filer;
	private final int threads;
	private final Threader threader;
	private final BlockingQueue<Record> queue;

	private RecordFinder(@Nonnull Filer filer, @Nonnull String path, int threads, int capacity) {
		this.filer = checkNotNull(filer);
		this.threads = threads;
		queue = new LinkedBlockingQueue<>(capacity);
		threader = new Threader(threads);
		findRecords(path);
	}

	private void findRecords(@Nonnull String path) {
		checkNotNull(path);

		Runnable runnable = () -> {
			try {
				for (Record record : filer.listRecords(path)) {
					Sleep.until(() -> queue.offer(record));
					if (record.isDir()) {
						findRecords(record.getPath());
					}
				}
			} catch (IOException e) {
				log.warn("failed to find records: " + path, e);
			}
		};

		if (threader.getRemaining() < threads) {
			threader.execute(path, runnable::run);
		} else {
			runnable.run();
		}
	}

	@Override
	public boolean tryAdvance(Consumer<? super Record> action) {
		while (threader.getRemaining() > 0 || queue.size() > 0) {
			try {
				Record record = queue.poll(10, TimeUnit.MILLISECONDS);
				if (record != null) {
					action.accept(record);
					return true;
				}
			} catch (InterruptedException e) {
				break;
			}
		}
		threader.close();
		return false;
	}

	@Nonnull
	public static Stream<Record> stream(@Nonnull Filer filer, @Nonnull String path, int threads) {
		return stream(filer, path, threads, 100000);
	}

	@Nonnull
	public static Stream<Record> stream(@Nonnull Filer filer, @Nonnull String path, int threads, int capacity) {
		return StreamSupport.stream(new RecordFinder(filer, path, threads, capacity), false);
	}
}
