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

import com.lithium.flow.util.Caches;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.cache.LoadingCache;

/**
 * @author Matt Ayres
 */
public class CachedAlerter implements Alerter {
	private final Alerter delegate;
	private final LoadingCache<String, State> alerts;

	public CachedAlerter(@Nonnull Alerter delegate) {
		this.delegate = checkNotNull(delegate);
		alerts = Caches.build(alert -> State.UNKNOWN);
	}


	@Override
	public void trigger(@Nonnull String alert, @Nonnull String description, @Nonnull Map<String, String> details) {
		if (changed(alert, State.TRIGGERED)) {
			delegate.trigger(alert, description, details);
		}
	}

	@Override
	public void resolve(@Nonnull String alert) {
		if (changed(alert, State.RESOLVED)) {
			delegate.resolve(alert);
		}
	}

	private boolean changed(@Nonnull String alert, @Nonnull State state) {
		synchronized (alerts) {
			if (alerts.getUnchecked(alert) != state) {
				alerts.put(alert, state);
				return true;
			}
			return false;
		}
	}

	private enum State {
		UNKNOWN,
		TRIGGERED,
		RESOLVED
	}
}
