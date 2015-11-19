package jp.gr.java_conf.uzresk.es.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

public class SkillFacets {

	public static void main(String[] args) {

		new SkillFacets().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			SearchResponse response = client.prepareSearch("tms-allflat")
					.setTypes("personal").setQuery(QueryBuilders.matchAllQuery())
					.addAggregation(AggregationBuilders
							.terms("aggs1").field("skill_1").size(20).order(Terms.Order.count(false)))
					.execute().actionGet();

			Terms terms = response.getAggregations().get("aggs1");
			terms.getBuckets().stream().forEach(s -> System.out.println(s.getKeyAsText() + "(" + s.getDocCount() + ")"));

		} finally {
			transportClient.close();
			client.close();
		}
	}
}
