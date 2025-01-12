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

import com.lithium.flow.util.Logs;

import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

public class LogAlerter implements Alerter {
	private static final Logger log = Logs.getLogger();

	private final Alerter delegate;

	public LogAlerter(@Nonnull Alerter delegate) {
		this.delegate = checkNotNull(delegate);
	}

	@Override
	public void trigger(@Nonnull String alert, @Nonnull String description, @Nonnull Map<String, String> details) {
		log.info("trigger: {} ({}) {}", alert, description, details);
		delegate.trigger(alert, description, details);
	}

	@Override
	public void resolve(@Nonnull String alert) {
		log.info("resolve: {}", alert);
		delegate.resolve(alert);
	}
}
