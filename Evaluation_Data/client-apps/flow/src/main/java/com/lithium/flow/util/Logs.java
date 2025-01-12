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

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.annotation.Nonnull;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Convenience methods to configure logging and get logger instances.
 *
 * @author Matt Ayres
 */
public class Logs {
	/**
	 * Configure logging with specified config, using defaults from "/log4j.properties" if it exists.
	 */
	public static void configure(@Nonnull Config config) throws IOException {
		checkNotNull(config);

		InputStream in = Logs.class.getResourceAsStream("/log4j.properties");
		if (in != null) {
			in.close();
			config = Configs.newBuilder().allowUndefined(true).include("/log4j.properties").addAll(config).build();
		}

		try {
			LogManager.resetConfiguration();
			PropertyConfigurator.configure(config.asProperties());
		} catch (NoClassDefFoundError e) {
			// log4j not available
		}
	}

	/**
	 * Return logger for calling class. Intended for static use as a class member field:
	 * <p>
	 * <code>private static final Logger log = Logs.getLogger();</code>
	 */
	@Nonnull
	public static Logger getLogger() {
		return getLogger(3);
	}

	@Nonnull
	static Logger getLogger(int index) {
		return LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[index].getClassName());
	}

	@Nonnull
	public static String message(String pattern, Object... objects) {
		return MessageFormatter.arrayFormat(pattern, objects).getMessage();
	}

	public static void redirect(@Nonnull Config config) {
		checkNotNull(config);
		if (config.getBoolean("log.stdout", false)) {
			Logs.redirectStdOut();
		}
		if (config.getBoolean("log.stderr", false)) {
			Logs.redirectStdErr();
		}
	}

	public static void redirectStdOut() {
		System.setOut(new PrintStream(new LogOutputStream(LoggerFactory.getLogger("stdout"), false), true));
	}

	public static void redirectStdErr() {
		System.setErr(new PrintStream(new LogOutputStream(LoggerFactory.getLogger("stderr"), true), true));
	}

	private static class LogOutputStream extends ByteArrayOutputStream {
		private final String separator = System.getProperty("line.separator");
		private final Logger log;
		private final boolean error;

		public LogOutputStream(@Nonnull Logger log, boolean error) {
			this.log = checkNotNull(log);
			this.error = error;
		}

		@Override
		public void write(byte[] b, int off, int len) {
			super.write(b, off, len);
			boolean isSeparator = count == separator.length() && separator.equals(toString());
			if (!isSeparator) {
				if (error) {
					log.error(toString());
				} else {
					log.info(toString());
				}
			}
			reset();
		}
	}
}
