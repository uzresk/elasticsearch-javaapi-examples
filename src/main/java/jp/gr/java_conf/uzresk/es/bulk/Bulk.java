package jp.gr.java_conf.uzresk.es.bulk;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class Bulk {

	public static void main(String[] args) {

		new Bulk().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));
			BulkRequestBuilder request = client.prepareBulk();
			request.add(client.prepareIndex("tms-allflat", "personal").setSource(loadIndex()));
			BulkResponse response = request.execute().actionGet();

			if (response.hasFailures()) {
				throw new RuntimeException(response.buildFailureMessage());
			} else {
				System.out.println("success.");
			}
		} finally {
			transportClient.close();
			client.close();
		}
	}

	private String loadIndex() {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				this.getClass().getResourceAsStream("/jp/gr/java_conf/uzresk/es/operation/bulk/bulk.json"), "UTF-8"))) {
			StringBuilder sb = new StringBuilder();
			while (true) {
				String text = br.readLine();
				if (text == null)
					break;
				sb.append(text);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
