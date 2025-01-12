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

import com.lithium.flow.config.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import it.sauronsoftware.cron4j.Predictor;
import it.sauronsoftware.cron4j.Scheduler;

/**
 * @author Matt Ayres
 */
public class CronMain {
	private static final Logger log = Logs.getLogger();

	private final DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
	private final String cron;
	private final TimeZone timeZone;

	public CronMain(@Nonnull Config config) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(config.getString("run.class"));
		boolean now = config.getBoolean("run.now", false);
		cron = config.getString("run.cron", "");
		timeZone = config.containsKey("run.tz")
				? TimeZone.getTimeZone(config.getString("run.tz"))
				: TimeZone.getDefault();

		boolean overlap = config.getBoolean("run.overlap", false); // deprecated, use run.concurrent
		int concurrent = config.getInt("run.concurrent", overlap ? Integer.MAX_VALUE : 1);
		Semaphore semaphore = new Semaphore(concurrent);

		Runnable runnable = () -> {
			if (semaphore.tryAcquire()) {
				try {
					Main.run(clazz, config);
					logNextRun();
				} catch (Exception e) {
					log.warn("cron run failed", e);
				} finally {
					semaphore.release();
				}
			}
		};

		if (now) {
			log.info("running now");
			runnable.run();
		}

		if (!cron.isEmpty()) {
			if (!now) {
				logNextRun();
			}

			Scheduler scheduler = new Scheduler();
			scheduler.schedule(cron, runnable);
			scheduler.setTimeZone(timeZone);
			scheduler.start();
			Sleep.forever();
		}

		log.info("done");
	}

	private void logNextRun() {
		Predictor predictor = new Predictor(cron);
		predictor.setTimeZone(timeZone);
		log.info("next run: {}", dateFormat.format(predictor.nextMatchingTime()));
	}

	public static void main(String[] args) {
		Main.run();
	}
}
