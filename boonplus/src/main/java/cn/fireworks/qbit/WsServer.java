package cn.fireworks.qbit;

import java.util.HashMap;
import java.util.Map;

import io.advantageous.qbit.http.server.HttpServer;
import io.advantageous.qbit.http.server.HttpServerBuilder;
import io.vertx.core.json.Json;

public class WsServer {
	public static void main(String[] args) {
		/* Create an HTTP server. */
		HttpServer httpServer = HttpServerBuilder.httpServerBuilder().setPort(8086).build();

		/* Setting up a request Consumer with Java 8 Lambda expression. */
		httpServer.setHttpRequestConsumer(httpRequest -> {

			Map<String, Object> results = new HashMap<>();
			results.put("method", httpRequest.getMethod());
			results.put("uri", httpRequest.getUri());
			results.put("body", httpRequest.getBodyAsString());
			results.put("headers", httpRequest.getHeaders());
			results.put("params", httpRequest.getParams());
			httpRequest.getReceiver().response(200, "application/json", Json.encodePrettily(results));
		});

		/* Setup WebSocket Server support. */
		httpServer.setWebSocketOnOpenConsumer(webSocket -> {
			webSocket.setTextMessageConsumer(message -> {
				String echo = "ECHO " + message;
				System.out.println(echo);
				webSocket.sendText(echo);
			});
		});

		/* Start the server. */
		httpServer.start();
	}

}
