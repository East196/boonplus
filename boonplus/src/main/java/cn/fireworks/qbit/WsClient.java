package cn.fireworks.qbit;

import io.advantageous.qbit.http.client.HttpClient;
import io.advantageous.qbit.http.client.HttpClientBuilder;
import io.advantageous.qbit.http.websocket.WebSocket;

public class WsClient {
	public static void main(String[] args) {
		/* Setup an httpClient. */
		HttpClient httpClient = HttpClientBuilder.httpClientBuilder().setPort(8086).build();
		httpClient.start();

		/* Setup the client websocket. */
		WebSocket webSocket = httpClient.createWebSocket("/websocket/rocket");

		/* Setup the text consumer. */
		webSocket.setTextMessageConsumer(message -> {
			System.out.println(message);
		});
		webSocket.openAndWait();

		/* Send some messages. */
		webSocket.sendText("Hi mom");
		webSocket.sendText("Hello World!");
	}

}
