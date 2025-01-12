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

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;

/**
 * @author Matt Ayres
 */
public class InjectDetailsAlerter implements Alerter {
	private final Alerter delegate;
	private final Map<String, String> inject;

	public InjectDetailsAlerter(@Nonnull Alerter delegate, @Nonnull Map<String, String> inject) {
		this.delegate = checkNotNull(delegate);
		this.inject = checkNotNull(inject);
	}

	@Override
	public void trigger(@Nonnull String alert, @Nonnull String description, @Nonnull Map<String, String> details) {
		Map<String, String> combined = ImmutableMap.<String, String>builder().putAll(inject).putAll(details).build();
		delegate.trigger(alert, description, combined);
	}

	@Override
	public void resolve(@Nonnull String alert) {
		delegate.resolve(alert);
	}
}
