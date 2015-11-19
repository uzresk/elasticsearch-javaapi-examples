package jp.gr.java_conf.uzresk.es.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;

public class AgeAvg {

	public static void main(String[] args) {

		new AgeAvg().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			SearchResponse response = client.prepareSearch("tms-allflat")
					.setTypes("personal").setQuery(QueryBuilders.matchAllQuery())
					.addAggregation(AggregationBuilders.avg("aggs1").field("age"))
					.execute().actionGet();

			InternalAvg avg = response.getAggregations().get("aggs1");
			
			System.out.println(avg.getValueAsString());
			
		} finally {
			transportClient.close();
			client.close();
		}
	}
}
