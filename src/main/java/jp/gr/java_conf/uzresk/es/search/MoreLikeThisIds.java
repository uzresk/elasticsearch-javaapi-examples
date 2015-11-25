package jp.gr.java_conf.uzresk.es.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

public class MoreLikeThisIds {

	public static void main(String[] args) {

		new MoreLikeThisIds().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			MoreLikeThisQueryBuilder builder = QueryBuilders.moreLikeThisQuery()
					.ids("AVE4s02v6tFRAgTb20Y9")
					.minTermFreq(1)
					.minDocFreq(1);

			SearchResponse response = client.prepareSearch("group").setTypes("community")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(builder).setExplain(true).execute()
					.actionGet();
			SearchHits hits = response.getHits();
			hits.forEach(s -> System.out.println(s.getScore() + ":" + s.getSourceAsString()));

		} finally {
			transportClient.close();
			client.close();
		}
	}
}
