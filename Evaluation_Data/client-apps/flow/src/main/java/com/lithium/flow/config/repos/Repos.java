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

package com.lithium.flow.config.repos;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Access;
import com.lithium.flow.config.Config;
import com.lithium.flow.config.ConfigBuilder;
import com.lithium.flow.config.Configs;
import com.lithium.flow.config.Repo;
import com.lithium.flow.config.exception.IllegalConfigException;
import com.lithium.flow.filer.CachedFiler;
import com.lithium.flow.filer.CachedReadFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.FilteredFiler;
import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.RegexPathPredicate;
import com.lithium.flow.ioc.Locator;
import com.lithium.flow.svn.LoginSvnProvider;
import com.lithium.flow.svn.PoolSvnProvider;
import com.lithium.flow.svn.SvnFiler;
import com.lithium.flow.svn.SvnProvider;
import com.lithium.flow.util.Checker;
import com.lithium.flow.util.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;


/**
 * @author Matt Ayres
 */
public class Repos {
	@Nonnull
	public static Repo buildRepo(@Nonnull Locator locator) throws IOException {
		return buildRepo(locator.getInstance(Config.class), locator.getInstance(Access.class));
	}

	@Nonnull
	public static Repo buildRepo(@Nonnull Config config, @Nonnull Access access) throws IOException {
		return buildRepo(config, access, (config1, access1) -> config2 -> config2);
	}

	@Nonnull
	public static Repo buildRepo(@Nonnull Config config, @Nonnull Access access,
			@Nonnull BiFunction<Config, Access, UnaryOperator<Config>> function) throws IOException {
		return buildRepo(config, access, function, repo -> repo, Configs::newBuilder, () -> true);
	}

	@Nonnull
	public static Repo buildRepo(@Nonnull Config config, @Nonnull Access access,
			@Nonnull BiFunction<Config, Access, UnaryOperator<Config>> function,
			@Nonnull UnaryOperator<Repo> operator, @Nonnull Supplier<ConfigBuilder> supplier,
			@Nonnull Checker checker) throws IOException {
		checkNotNull(config);
		checkNotNull(access);
		checkNotNull(function);
		checkNotNull(supplier);
		checkNotNull(checker);

		List<Filer> filers = new ArrayList<>();
		for (String url : config.getList("configs.url")) {
			filers.add(buildFiler(config, access, url));
		}

		List<String> paths = config.getList("configs.path", Collections.singletonList("prod"));
		long scheduleInterval = config.getTime("configs.scheduleInterval", "1h");
		long cacheTime = config.getTime("configs.cacheTime", "5m");

		Repo repo = new FilerRepo(filers, paths, ".config", function.apply(config, access), supplier);
		repo = new FilteredRepo(repo, config);
		repo = new ParallelRepo(repo, config.prefix("configs"));
		repo = operator.apply(repo);
		if (scheduleInterval > 0) {
			long scheduleOffset = config.getTime("configs.scheduleOffset", "0");
			long scheduleRandom = config.getTime("configs.scheduleRandom", String.valueOf(scheduleInterval));
			if (scheduleRandom > 0) {
				scheduleOffset += Math.abs(new Random().nextLong()) % scheduleRandom;
			}
			repo = new ScheduledRepo(repo, scheduleInterval, scheduleOffset, checker);
		} else if (cacheTime > 0) {
			repo = new CachedRepo(repo, cacheTime);
		}

		return repo;
	}

	@Nonnull
	public static Filer buildFiler(@Nonnull Config config, @Nonnull Access access, @Nonnull String url)
			throws IOException {
		checkNotNull(config);
		checkNotNull(url);
		checkNotNull(access);

		try {
			SvnProvider svnProvider = new LoginSvnProvider(config, access, url);
			svnProvider = new PoolSvnProvider(svnProvider, config);

			long revision = -1;
			boolean hasDate = config.containsKey("configs.date");
			boolean hasRevision = config.containsKey("configs.revision");
			boolean findLast = config.getBoolean("configs.findLast", false);

			if (hasDate && hasRevision) {
				throw new IllegalConfigException("both 'configs.date' and 'configs.revision' are set");
			} else if (hasDate) {
				String date = config.getString("configs.date");
				SVNRepository repository = svnProvider.getRepository();
				try {
					revision = repository.getDatedRevision(new Date(DateUtils.toMillis(date)));
				} finally {
					svnProvider.releaseRepository(repository);
				}
			} else if (hasRevision) {
				revision = config.getLong("configs.revision");
			}

			Filer filer = new SvnFiler(svnProvider, revision, findLast);
			filer = new CachedFiler(filer, config.prefix("configs"));
			filer = new CachedReadFiler(filer);
			if (config.containsKey("configs.exclude")) {
				Predicate<Record> predicate = new RegexPathPredicate(config.getList("configs.exclude")).negate();
				filer = new FilteredFiler(filer, predicate);
			}
			return filer;
		} catch (SVNException e) {
			throw new IOException("failed to build filer: " + url, e);
		}
	}
}
