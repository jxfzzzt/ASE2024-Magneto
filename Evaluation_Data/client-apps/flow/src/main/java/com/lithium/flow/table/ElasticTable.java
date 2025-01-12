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

package com.lithium.flow.table;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import com.lithium.flow.util.Unchecked;

import java.util.List;

import javax.annotation.Nonnull;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

/**
 * @author Matt Ayres
 */
public class ElasticTable implements Table {
	private final Client client;
	private final String index;

	@Deprecated
	@SuppressWarnings("unused")
	public ElasticTable(@Nonnull Client client, @Nonnull String index, @Nonnull String type) {
		this.client = checkNotNull(client);
		this.index = checkNotNull(index);
	}

	public ElasticTable(@Nonnull Client client, @Nonnull String index) {
		this.client = checkNotNull(client);
		this.index = checkNotNull(index);
	}

	@Override
	@Nonnull
	public Row getRow(@Nonnull Key key) {
		SearchHits hits = client.prepareSearch(index)
				.setQuery(QueryBuilders.termQuery("_id", key.id()))
				.execute().actionGet().getHits();

		Row row = new Row(key);
		if (hits.getHits().length > 0) {
			row.putAll(hits.getAt(0).getSourceAsMap());
		}
		return row;
	}

	@Override
	public void putRow(@Nonnull Row row) {
		indexRequest(row).execute().actionGet();
	}

	@Override
	public void deleteRow(@Nonnull Key key) {
		client.prepareDelete(index, "_doc", key.id()).execute().actionGet();
	}

	@Override
	public void putRows(@Nonnull List<Row> rows) {
		if (rows.size() > 0) {
			BulkRequestBuilder request = client.prepareBulk();
			rows.forEach(row -> request.add(indexRequest(row)));

			BulkResponse response = request.execute().actionGet();
			if (response.hasFailures()) {
				throw new RuntimeException(response.buildFailureMessage());
			}
		}
	}

	@Nonnull
	private IndexRequestBuilder indexRequest(@Nonnull Row row) {
		return Unchecked.get(() -> {
			XContentBuilder content = jsonBuilder().startObject();
			for (String column : row.columns()) {
				content.field(column, row.getCell(column, Object.class));
			}
			String id = row.getKey().isAuto() ? null : row.getKey().id();
			return client.prepareIndex(index, "_doc", id).setSource(content.endObject());
		});
	}

	@Override
	public void close() {
		client.close();
	}
}
