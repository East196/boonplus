package com.github.east196.lab.ws;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
  
@WebSocket
public class SimpleEchoHandler extends WebSocketHandler {
  
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
    }
  
    @OnWebSocketError
    public void onError(Throwable t) {
    }
  
    @OnWebSocketConnect
    public void onConnect(Session session) {
    }
  
    @OnWebSocketMessage
    public void onMessage(String message) {
    	System.out.println(message + " in server......");
    }
  
    @Override
    public void configure(WebSocketServletFactory factory) {
        // TODO Auto-generated method stub
        factory.register(SimpleEchoHandler.class);
    }
}