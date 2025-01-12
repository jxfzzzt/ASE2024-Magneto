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
import static org.apache.lucene.index.IndexWriterConfig.OpenMode;

import com.lithium.flow.config.Config;
import com.lithium.flow.filer.DecoratedFiler;
import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Record;
import com.lithium.flow.io.DecoratedOutputStream;
import com.lithium.flow.util.Logs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NRTCachingDirectory;
import org.slf4j.Logger;

/**
 * @author Matt Ayres
 */
public class LuceneFiler extends DecoratedFiler {
	private static final Logger log = Logs.getLogger();

	private final IndexWriter writer;
	private final ReferenceManager<IndexSearcher> manager;
	private final ControlledRealTimeReopenThread<IndexSearcher> thread;
	private final long maxAge;

	public LuceneFiler(@Nonnull Filer delegate, @Nonnull Config config) throws IOException {
		super(delegate);

		String path = config.getString("index.path");
		maxAge = config.getTime("index.maxAge", "-1");
		double maxMergeMb = config.getDouble("index.maxMergeMb", 4);
		double maxCachedMb = config.getDouble("index.maxCacheMb", 64);
		long targetMaxStale = config.getTime("index.targetMaxStale", "5s");
		long targetMinStale = config.getTime("index.targetMinStale", "1s");

		Directory dir = FSDirectory.open(new File(path).toPath());
		NRTCachingDirectory cachingDir = new NRTCachingDirectory(dir, maxMergeMb, maxCachedMb);
		IndexWriterConfig writerConfig = new IndexWriterConfig(null);
		writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

		writer = new IndexWriter(cachingDir, writerConfig);
		manager = new SearcherManager(writer, true, true, new SearcherFactory());
		thread = new ControlledRealTimeReopenThread<>(writer, manager, targetMaxStale, targetMinStale);
		thread.start();
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		checkNotNull(path);

		Term term = RecordDoc.getTermForParent(path);
		List<Record> records = search(term);
		if (records == null) {
			records = super.listRecords(path);
			records.forEach(this::writeRecord);
		}
		return records;
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		checkNotNull(path);

		Term term = RecordDoc.getTermForPath(path);
		List<Record> records = search(term);
		if (records == null || records.size() == 0) {
			Record record = super.getRecord(path);
			writeRecord(record);
			return record;
		} else {
			return records.get(0);
		}
	}

	@Override
	@Nonnull
	public Stream<Record> findRecords(@Nonnull String path, int threads) throws IOException {
		boolean useDelegate = false;
		try {
			Term term = RecordDoc.getTermForParent(path);
			List<Record> records = search(term);
			if (records == null || records.size() == 0) {
				useDelegate = true;
			}
		} catch (IOException e) {
			useDelegate = true;
		}

		if (useDelegate) {
			return super.findRecords(path, threads).map(this::writeRecord);
		} else {
			return super.findRecords(path, threads);
		}
	}

	@Nullable
	private List<Record> search(@Nonnull Term term) throws IOException {
		long time = System.currentTimeMillis();

		manager.maybeRefresh();
		IndexSearcher searcher = manager.acquire();
		try {
			TopDocs topDocs = searcher.search(new TermQuery(term), Integer.MAX_VALUE);
			if (topDocs.totalHits.value > 0) {
				List<Record> records = new ArrayList<>();
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					Document doc = searcher.doc(scoreDoc.doc);
					RecordDoc recordDoc = RecordDoc.create(doc);
					records.add(recordDoc.getRecord());
					if (maxAge > -1 && time > recordDoc.getIndexTime() + maxAge) {
						return null;
					}
				}
				return records;
			}
		} finally {
			manager.release(searcher);
		}

		return null;
	}

	@Nonnull
	private Record writeRecord(@Nonnull Record record) {
		RecordDoc recordDoc = RecordDoc.create(record, System.currentTimeMillis());
		try {
			writer.updateDocument(recordDoc.getTerm(), recordDoc.getDocument());
		} catch (IOException e) {
			log.warn("failed to update document: {}", record, e);
		}
		return record;
	}

	private void deleteRecord(@Nonnull String path) throws IOException {
		writer.deleteDocuments(RecordDoc.getTermForPath(path));
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		deleteRecord(path);
		return new DecoratedOutputStream(super.writeFile(path)) {
			@Override
			public void close() throws IOException {
				super.close();
				deleteRecord(path);
			}
		};
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		deleteRecord(path);
		super.setFileTime(path, time);
	}

	@Override
	public void close() throws IOException {
		super.close();
		thread.close();
		writer.close();
	}
}
