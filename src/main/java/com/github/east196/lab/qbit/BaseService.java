package com.github.east196.lab.qbit;

import io.advantageous.qbit.annotation.RequestMapping;

@RequestMapping("/base-service")
public class BaseService {

	@RequestMapping("/port")
	public String  port() {
		return "本服务器监听端口:"+Server.port;
	}
	
}