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

package com.lithium.flow.access;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

/**
 * @author Matt Ayres
 */
public interface Prompt {
	@Nonnull
	Response prompt(@Nonnull String name, @Nonnull String message, @Nonnull Type type);

	enum Type {
		PLAIN, MASKED, BLOCK
	}

	interface Response {
		@Nonnull
		String value();

		@Nonnull
		String accept();

		void reject();

		@Nonnull
		static Response build(@Nonnull String value) {
			return build(value, valid -> {
			});
		}

		@Nonnull
		static Response build(@Nonnull String value, @Nonnull Consumer<Boolean> consumer) {
			return new Response() {
				@Override
				@Nonnull
				public String value() {
					return value;
				}

				@Override
				@Nonnull
				public String accept() {
					consumer.accept(true);
					return value;
				}

				@Override
				public void reject() {
					consumer.accept(false);
				}
			};
		}
	}
}
