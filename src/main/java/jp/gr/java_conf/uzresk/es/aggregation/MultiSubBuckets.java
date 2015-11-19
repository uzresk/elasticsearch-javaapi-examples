package jp.gr.java_conf.uzresk.es.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

public class MultiSubBuckets {

	public static void main(String[] args) {

		new MultiSubBuckets().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			SearchResponse response = client.prepareSearch("tms-allflat").setTypes("personal")
					.setQuery(QueryBuilders.matchAllQuery())
					.addAggregation(AggregationBuilders.terms("aggs1").field("skill_1").size(20)
							.order(Terms.Order.count(false))
							.subAggregation(AggregationBuilders.terms("aggs2").field("gender").size(2)
							.subAggregation(AggregationBuilders.histogram("aggs3").field("age").interval(10))))
					.execute().actionGet();

			Terms terms = response.getAggregations().get("aggs1");
			for (Bucket aggs1 : terms.getBuckets()) {

				System.out.println(aggs1.getKey() + "(" + aggs1.getDocCount() + ")");

				Terms strTerms = aggs1.getAggregations().get("aggs2");

				for (Bucket aggs2 : strTerms.getBuckets()) {
					System.out.println("\t" + aggs2.getKey() + "(" + aggs2.getDocCount() + ")");

					Histogram histgram = aggs2.getAggregations().get("aggs3");
					for (Histogram.Bucket aggs3 : histgram.getBuckets()) {
						System.out.println("\t\t" + aggs3.getKey() + "(" + aggs3.getDocCount() + ")");
					}
				}

			}
		} finally {
			transportClient.close();
			client.close();
		}
	}
}
