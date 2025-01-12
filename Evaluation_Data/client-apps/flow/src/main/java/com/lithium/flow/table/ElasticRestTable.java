/*
 * Copyright 2020 Lithium Technologies, Inc.
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

package com.lithium.flow.table;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author Matt Ayres
 */
public class ElasticRestTable implements Table {
	private final RestHighLevelClient client;
	private final String index;

	public ElasticRestTable(@Nonnull RestHighLevelClient client, @Nonnull String index) {
		this.client = checkNotNull(client);
		this.index = checkNotNull(index);
	}

	@Override
	@Nonnull
	public Row getRow(@Nonnull Key key) {
		try {
			SearchSourceBuilder source = new SearchSourceBuilder();
			source.query(QueryBuilders.termQuery("_id", key.id()));

			SearchRequest request = new SearchRequest();
			request.indices(index);
			request.source(source);

			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			SearchHits hits = response.getHits();

			Row row = new Row(key);
			if (hits.getHits().length > 0) {
				row.putAll(hits.getAt(0).getSourceAsMap());
			}
			return row;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void putRow(@Nonnull Row row) {
		try {
			client.index(indexRequest(row), RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void deleteRow(@Nonnull Key key) {
		try {
			DeleteRequest request = new DeleteRequest();
			request.index(index);
			request.id(key.id());

			client.delete(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void putRows(@Nonnull List<Row> rows) {
		if (rows.size() == 0) {
			return;
		}

		try {
			BulkRequest request = new BulkRequest();
			for (Row row : rows) {
				request.add(indexRequest(row));
			}

			BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
			if (response.hasFailures()) {
				throw new RuntimeException(response.buildFailureMessage());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	private IndexRequest indexRequest(@Nonnull Row row) throws IOException {
		String id = row.getKey().isAuto() ? null : row.getKey().id();

		XContentBuilder content = jsonBuilder().startObject();
		for (String column : row.columns()) {
			content.field(column, row.getCell(column, Object.class));
		}

		IndexRequest request = new IndexRequest();
		request.index(index);
		request.id(id);
		request.source(content.endObject());
		return request;
	}

	@Override
	public void close() throws IOException {
		client.close();
	}
}
