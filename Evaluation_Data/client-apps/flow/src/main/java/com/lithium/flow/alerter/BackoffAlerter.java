/*
 * Copyright 2016 Lithium Technologies, Inc.
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

package com.lithium.flow.alerter;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public class BackoffAlerter implements Alerter {
	private final Alerter delegate;
	private final long backoffTime;
	private final Map<String, Long> alerts = new HashMap<>();

	public BackoffAlerter(@Nonnull Alerter delegate, long backoffTime) {
		this.delegate = checkNotNull(delegate);
		this.backoffTime = backoffTime;
	}

	@Override
	public void trigger(@Nonnull String alert, @Nonnull String description, @Nonnull Map<String, String> details) {
		long time = System.currentTimeMillis();
		boolean trigger = false;

		synchronized (alerts) {
			long lastTime = alerts.getOrDefault(alert, 0L);
			if (time - lastTime > backoffTime) {
				alerts.put(alert, time);
				trigger = true;
			}
		}

		if (trigger) {
			delegate.trigger(alert, description, details);
		}
	}

	@Override
	public void resolve(@Nonnull String alert) {
		delegate.resolve(alert);
	}
}
