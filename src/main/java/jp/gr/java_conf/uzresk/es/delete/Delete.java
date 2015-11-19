package jp.gr.java_conf.uzresk.es.delete;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class Delete {

	public static void main(String[] args) {

		new Delete().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			
		} finally {
			transportClient.close();
			client.close();
		}
	}
}
