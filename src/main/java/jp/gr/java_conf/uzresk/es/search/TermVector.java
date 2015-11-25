package jp.gr.java_conf.uzresk.es.search;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.termvector.TermVectorResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentHelper;

public class TermVector {

	public static void main(String[] args) {

		new TermVector().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			TermVectorResponse response = client.prepareTermVector("group", "community", "AVE4s02v6tFRAgTb20Y9")
					.setTermStatistics(true).setFieldStatistics(true).setPayloads(true).setSelectedFields("description").execute().actionGet();

			
			try {
				XContentBuilder builder = XContentFactory.jsonBuilder();
				builder.startObject();
				response.toXContent(builder, ToXContent.EMPTY_PARAMS);
				builder.endObject();
				System.out.println(builder.string());

				Map<String, Object> map = XContentHelper.convertToMap(builder.bytes(), false).v2();
				map.entrySet().stream().forEach(s -> System.out.println(s.getKey() + ":" + s.getValue()));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} finally {
			transportClient.close();
			client.close();
		}
	}
}
