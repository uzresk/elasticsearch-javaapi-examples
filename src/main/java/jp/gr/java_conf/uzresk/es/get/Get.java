package jp.gr.java_conf.uzresk.es.get;

import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class Get {

	public static void main(String[] args) {

		new Get().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			GetResponse response = client.prepareGet("tms-allflat", "personal", "AVEdyTXM9PWe3gelUGEO").execute()
					.actionGet();

			Map<String, Object> result = response.getSourceAsMap();
			result.entrySet().stream().forEach(s -> System.out.println(s.getKey() + ":" + s.getValue()));

		} finally {
			transportClient.close();
			client.close();
		}
	}
}
