package jp.gr.java_conf.uzresk.es.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

public class SubBuckets {

	public static void main(String[] args) {

		new SubBuckets().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			SearchResponse response = client.prepareSearch("tms-allflat").setTypes("personal")
					.setQuery(QueryBuilders.matchAllQuery())
					.addAggregation(
							AggregationBuilders.terms("aggs1").field("skill_1").size(20).order(Terms.Order.count(false))
									.subAggregation(AggregationBuilders.terms("aggs2").field("gender").size(2)))
					.execute().actionGet();

			Terms terms = response.getAggregations().get("aggs1");
			for (Bucket aggs1 : terms.getBuckets()) {

				System.out.println(aggs1.getKey() + "(" + aggs1.getDocCount() + ")");

				Terms subTerms = aggs1.getAggregations().get("aggs2");

				for (Bucket aggs2 : subTerms.getBuckets()) {
					System.out.println("\t" + aggs2.getKey() + "(" + aggs2.getDocCount() + ")");
				}

			}
		} finally {
			transportClient.close();
			client.close();
		}
	}
}
