package jp.gr.java_conf.uzresk.es.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;

public class AgeStats {

	public static void main(String[] args) {

		new AgeStats().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			SearchResponse response = client.prepareSearch("tms-allflat").setTypes("personal")
					.setQuery(QueryBuilders.matchAllQuery())
					.addAggregation(AggregationBuilders.stats("aggs1").field("age")).execute().actionGet();

			Stats stats = response.getAggregations().get("aggs1");

			System.out.println("avg:" + stats.getAvg());
			System.out.println("min:" + stats.getMin());
			System.out.println("max:" + stats.getMax());
			System.out.println("sum:" + stats.getSum());
			System.out.println("count:" + stats.getCount());

		} finally {
			transportClient.close();
			client.close();
		}
	}
}
