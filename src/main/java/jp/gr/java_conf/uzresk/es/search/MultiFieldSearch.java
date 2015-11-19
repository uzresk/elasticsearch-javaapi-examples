package jp.gr.java_conf.uzresk.es.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHits;

public class MultiFieldSearch {

	public static void main(String[] args) {

		new MultiFieldSearch().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			SearchResponse response = client.prepareSearch("tms-allflat").setTypes("personal")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(new MultiMatchQueryBuilder("Java","skill_1^10","skill_3"))
					.setPostFilter(FilterBuilders.rangeFilter("age").from(25).to(30)).setFrom(0).setSize(10)
					.setExplain(true).execute().actionGet();

			SearchHits hits = response.getHits();
			hits.forEach(s -> System.out.println(s.getScore() + ":" + s.getSourceAsString()));

		} finally {
			transportClient.close();
			client.close();
		}
	}
}
