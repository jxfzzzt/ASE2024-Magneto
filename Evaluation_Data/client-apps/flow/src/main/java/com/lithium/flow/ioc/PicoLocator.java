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

package com.lithium.flow.ioc;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.picocontainer.Characteristics.CACHE;

import com.lithium.flow.util.Mutex;
import com.lithium.flow.util.Mutexes;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.picocontainer.adapters.AbstractAdapter;

import com.google.common.io.Closer;

/**
 * @author Matt Ayres
 */
public class PicoLocator implements Locator {
	private final MutablePicoContainer pico;
	private final Mutexes<Class<?>> mutexes;
	private final Set<Class<?>> types = Collections.synchronizedSet(new HashSet<>());

	public PicoLocator() {
		this(null);
	}

	public PicoLocator(@Nullable PicoLocator parentLocator) {
		if (parentLocator == null) {
			pico = new DefaultPicoContainer().as(CACHE);
			mutexes = new Mutexes<>();
		} else {
			pico = new DefaultPicoContainer(parentLocator.pico).as(CACHE);
			mutexes = parentLocator.mutexes;
		}
	}

	@Override
	public void addType(@Nonnull Class<?> type) {
		checkNotNull(type);
		pico.addComponent(type);
		types.add(type);
	}

	@Override
	public void addProvider(@Nonnull Class<?> type, @Nonnull Provider<?> provider) {
		checkNotNull(type);
		checkNotNull(provider);
		if (types.stream().noneMatch(type::isAssignableFrom)) {
			pico.addAdapter(new Adapter(type, provider));
			types.add(type);
		}
	}

	@Override
	public void addInstance(@Nonnull Object object) {
		checkNotNull(object);
		pico.addComponent(object);
		types.add(object.getClass());
	}

	@Override
	public void addInstance(@Nonnull Object key, @Nonnull Object object) {
		checkNotNull(key);
		checkNotNull(object);
		pico.addComponent(key, object);
		types.add(object.getClass());
	}

	@Override
	@Nonnull
	public <T> T getInstance(@Nonnull Class<T> type) {
		checkNotNull(type);
		try (Mutex ignored = mutexes.getMutex(type)) {
			T instance = pico.getComponent(type);
			if (instance == null) {
				throw new RuntimeException("no instance for type: " + type);
			}
			return instance;
		}
	}

	@Override
	@Nonnull
	public List<Object> getInstances() {
		List<Object> objects = new ArrayList<>();

		PicoContainer nextPico = pico;
		do {
			objects.addAll(nextPico.getComponents());
			nextPico = nextPico.getParent();
		} while (nextPico != null);

		return objects;
	}

	@Override
	@Nonnull
	public Locator createChild() {
		return new PicoLocator(this);
	}

	@Override
	public void close() throws IOException {
		try (Closer closer = Closer.create()) {
			getInstances().stream()
					.filter(object -> Closeable.class.isAssignableFrom(object.getClass()))
					.map(object -> (Closeable) object)
					.forEach(closer::register);
		}
	}

	public class Adapter extends AbstractAdapter<Object> {
		private final Provider<?> provider;

		public Adapter(@Nonnull Class<?> type, @Nonnull Provider<?> provider) {
			super(type, type);
			this.provider = provider;
		}

		@Override
		@Nonnull
		public Object getComponentInstance(@Nonnull PicoContainer container, @Nullable Type into)
				throws PicoCompositionException {
			try {
				return provider.provide(PicoLocator.this);
			} catch (Exception e) {
				throw new PicoCompositionException("failed to provide type: " + into, e);
			}
		}

		@Override
		public void verify(@Nonnull PicoContainer container) throws PicoCompositionException {
		}

		@Override
		@Nonnull
		public String getDescriptor() {
			return getComponentKey().toString();
		}
	}
}
