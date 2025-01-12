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

import com.lithium.flow.config.Config;
import com.lithium.flow.util.Logs;
import com.lithium.flow.util.Sleep;
import com.lithium.flow.util.TimeUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import com.squareup.pagerduty.incidents.NotifyResult;
import com.squareup.pagerduty.incidents.PagerDuty;
import com.squareup.pagerduty.incidents.Resolution;
import com.squareup.pagerduty.incidents.Trigger;

public class PagerDutyAlerter implements Alerter {
	private static final Logger log = Logs.getLogger();

	private final PagerDuty client;
	private final long[] delays;

	public PagerDutyAlerter(@Nonnull Config config) {
		checkNotNull(config);

		client = PagerDuty.create(config.getString("pagerduty.key"));
		delays = config.getList("pagerduty.delays", Arrays.asList("5s", "15s", "30s", "60s"))
				.stream().mapToLong(TimeUtils::getMillisValue).toArray();
	}

	@Override
	public void trigger(@Nonnull String alert, @Nonnull String description, @Nonnull Map<String, String> details) {
		checkNotNull(alert);
		checkNotNull(description);
		checkNotNull(details);

		notify(new Trigger.Builder(description).withIncidentKey(alert).addDetails(details).build());
	}

	public void resolve(@Nonnull String alert) {
		checkNotNull(alert);

		notify(new Resolution.Builder(alert).build());
	}

	private void notify(@Nonnull Object notify) {
		for (long delay : delays) {
			try {
				log.debug("notify: {}", ToStringBuilder.reflectionToString(notify, ToStringStyle.SHORT_PREFIX_STYLE));

				NotifyResult result;
				if (notify instanceof Trigger) {
					result = client.notify((Trigger) notify);
				} else {
					result = client.notify((Resolution) notify);
				}

				log.debug("result: {}", ToStringBuilder.reflectionToString(result, ToStringStyle.SHORT_PREFIX_STYLE));

				if (result.status().equals("success")) {
					return;
				}

				log.warn("notify failed: {} ({})", result.status(), result.message());
			} catch (IOException e) {
				log.warn("notify failed", e);
			}

			log.info("retrying in {}ms", delay);
			Sleep.softly(delay);
		}
	}
}
