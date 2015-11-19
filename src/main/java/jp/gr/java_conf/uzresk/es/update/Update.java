package jp.gr.java_conf.uzresk.es.update;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class Update {

	public static void main(String[] args) {

		new Update().run();
	}

	public void run() {
		Client client = null;
		TransportClient transportClient = null;
		try {
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress("192.168.1.40", 9300));

			Map<String, String> map = new HashMap<>();
			map.put("library", "seasar,struts,spring-boot");

			UpdateRequest request = new UpdateRequest("tms-allflat", "personal", "AVEdzC6G9PWe3gelUGEP");
			request.doc(map);

			client.update(request).get();
			
			System.out.println("update finished.");
			
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} finally {
			transportClient.close();
			client.close();
		}
	}
}
