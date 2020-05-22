package com.github.east196.lab.ws;

import org.eclipse.jetty.server.Server;

public class SimpleEchoServer {
	public static void main(String[] args) throws Exception {
	    Server server = new Server(2014);
	    server.setHandler(new SimpleEchoHandler());
	    server.setStopTimeout(0);
	    server.start();
	    server.join();
	}
}
