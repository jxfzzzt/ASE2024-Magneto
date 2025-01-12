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

package com.lithium.flow.filer.lucene;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.lucene.document.Field.Store;

import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.RecordPath;

import java.net.URI;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;

/**
 * @author Matt Ayres
 */
public class RecordDoc {
	public static final String RECORD_URI = "record.uri";
	public static final String RECORD_PATH = "record.path";
	public static final String RECORD_PARENT = "record.parent";
	public static final String RECORD_NAME = "record.name";
	public static final String RECORD_TIME = "record.time";
	public static final String RECORD_SIZE = "record.size";
	public static final String RECORD_DIR = "record.dir";
	public static final String INDEX_TIME = "index.time";

	private final Record record;
	private final Document doc;
	private final long indexTime;

	public RecordDoc(@Nonnull Record record, @Nonnull Document doc, long indexTime) {
		this.record = checkNotNull(record);
		this.doc = checkNotNull(doc);
		this.indexTime = indexTime;
	}

	@Nonnull
	public Term getTerm() {
		return new Term(RECORD_PATH, record.getPath());
	}

	@Nonnull
	public Document getDocument() {
		return doc;
	}

	@Nonnull
	public Record getRecord() {
		return record;
	}

	public long getIndexTime() {
		return indexTime;
	}

	@Nonnull
	public static RecordDoc create(@Nonnull Record record, long indexTime) {
		checkNotNull(record);

		Document doc = new Document();
		doc.add(new StringField(RECORD_URI, record.getUri().toString(), Store.YES));
		doc.add(new StringField(RECORD_PATH, record.getPath(), Store.YES));
		doc.add(new StringField(RECORD_PARENT, record.getFolder(), Store.YES));
		doc.add(new StringField(RECORD_NAME, record.getName(), Store.YES));
		doc.add(new StringField(RECORD_TIME, String.valueOf(record.getTime()), Store.YES));
		doc.add(new StringField(RECORD_SIZE, String.valueOf(record.getSize()), Store.YES));
		doc.add(new StringField(RECORD_DIR, String.valueOf(record.isDir()), Store.YES));
		doc.add(new StringField(INDEX_TIME, String.valueOf(indexTime), Store.YES));

		return new RecordDoc(record, doc, indexTime);
	}

	@Nonnull
	public static RecordDoc create(@Nonnull Document doc) {
		checkNotNull(doc);

		URI uri = URI.create(doc.get(RECORD_URI));
		String parent = doc.get(RECORD_PARENT);
		String name = doc.get(RECORD_NAME);
		long time = Long.parseLong(doc.get(RECORD_TIME));
		long size = Long.parseLong(doc.get(RECORD_SIZE));
		boolean dir = Boolean.parseBoolean(doc.get(RECORD_DIR));
		long indexTime = Long.parseLong(doc.get(INDEX_TIME));

		Record record = new Record(uri, RecordPath.from(parent, name), time, size, dir);
		return new RecordDoc(record, doc, indexTime);
	}

	@Nonnull
	public static Term getTermForPath(String path) {
		return new Term(RECORD_PATH, path);
	}

	@Nonnull
	public static Term getTermForParent(String parent) {
		return new Term(RECORD_PARENT, parent);
	}

	@Override
	@Nonnull
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
