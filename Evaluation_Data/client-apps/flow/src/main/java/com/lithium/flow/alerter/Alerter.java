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

import com.lithium.flow.config.Config;
import com.lithium.flow.config.exception.IllegalConfigException;
import com.lithium.flow.util.Unchecked;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public interface Alerter {
	default void trigger(@Nonnull String alert, @Nonnull String description) {
		trigger(alert, description, Collections.emptyMap());
	}

	void trigger(@Nonnull String alert, @Nonnull String description, @Nonnull Map<String, String> details);

	void resolve(@Nonnull String alert);

	@Nonnull
	static Alerter build(@Nonnull Config config) {
		return decorate(construct(config), config);
	}

	@Nonnull
	static Alerter construct(@Nonnull Config config) {
		String name = config.getString("alerter", "none");
		switch (name) {
			case "none":
				return new NoAlerter();

			case "log":
				return new LogAlerter(new NoAlerter());

			case "pagerduty":
				return new PagerDutyAlerter(config);

			default:
				throw new IllegalConfigException("alerter", name, "String", null);

		}
	}

	@Nonnull
	static Alerter decorate(@Nonnull Alerter alerter, @Nonnull Config config) {
		if (config.getBoolean("alerter.log", true) && !(alerter instanceof LogAlerter)) {
			alerter = new LogAlerter(alerter);
		}

		double throttle = config.getDouble("alerter.throttle", 0.5);
		if (throttle > 0) {
			alerter = new ThrottleAlerter(alerter, throttle);
		}

		Map<String, String> injects = new LinkedHashMap<>();
		for (String inject : config.getList("alerter.inject", Arrays.asList("app", "host"))) {
			switch (inject) {
				case "app":
					injects.put("app", Thread.currentThread().getStackTrace()[3].getClassName());
					break;

				case "host":
					injects.put("host", Unchecked.get(() -> InetAddress.getLocalHost().getHostName()));
					break;

				default:
					throw new RuntimeException("unknown alerter inject: " + inject);
			}
		}
		if (injects.size() > 0) {
			alerter = new InjectDetailsAlerter(alerter, injects);
		}

		if (config.getBoolean("alerter.cached", true)) {
			alerter = new CachedAlerter(alerter);
		}

		long backoffTime = config.getTime("alerter.backoff", "0");
		if (backoffTime > 0) {
			alerter = new BackoffAlerter(alerter, backoffTime);
		}

		return alerter;
	}
}
