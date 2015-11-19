package jp.gr.java_conf.uzresk.es.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

public class MultipleKeywordSearch {

	public static void main(String[] args) {

		new MultipleKeywordSearch().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder.should(QueryBuilders.termQuery("skill_1", "Java"));
			boolQueryBuilder.should(QueryBuilders.termQuery("skill_1", "Ruby").boost(5f));
			
			SearchResponse response = client.prepareSearch("tms-allflat").setTypes("personal")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(boolQueryBuilder)
					.addSort(SortBuilders.scoreSort().order(SortOrder.ASC))
					.addSort(SortBuilders.fieldSort("age").order(SortOrder.ASC))
					.setPostFilter(FilterBuilders.rangeFilter("age").from(25).to(30)).setFrom(0).setSize(1000)
					.setExplain(true).execute().actionGet();

			SearchHits hits = response.getHits();
			hits.forEach(s -> System.out.println(s.getScore() + ":" + s.getSourceAsString()));

		} finally {
			transportClient.close();
			client.close();
		}
	}
}
