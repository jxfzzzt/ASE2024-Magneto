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

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Decorates an instance of {@link Filer} to sort file details by name in ascending or descending order.
 * 
 * @author Matt Ayres
 */
public class SortedFiler extends DecoratedFiler {
	public static final Comparator<Record> NAME_ASC = Comparator.comparing(Record::getName);

	public static final Comparator<Record> NAME_DESC = (d1, d2) -> d2.getName().compareTo(d1.getName());

	private final Comparator<Record> comparator;

	public SortedFiler(@Nonnull Filer delegate) {
		this(delegate, NAME_ASC);
	}

	public SortedFiler(@Nonnull Filer delegate, @Nonnull Comparator<Record> comparator) {
		super(checkNotNull(delegate));
		this.comparator = checkNotNull(comparator);
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		List<Record> records = super.listRecords(path);
		records.sort(comparator);
		return records;
	}
}
