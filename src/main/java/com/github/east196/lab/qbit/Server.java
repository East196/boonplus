package com.github.east196.lab.qbit;

import io.advantageous.qbit.server.ServiceServer;
import io.advantageous.qbit.server.ServiceServerBuilder;

public class Server {
	
	public static int port=0;
	
	public static void main(String... args) {
		if(args.length!=1){
			System.out.println("参数数目有误,自动设置端口号为12301!");
			port=12301;
		}else{
			port=Integer.parseInt(args[0]);
		}
	    ServiceServer server = new ServiceServerBuilder().setPort(port).build();
	    server.initServices(new TodoService(),new AdderService(),new BaseService());
	    server.start();
	}
}

