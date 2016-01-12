package cn.fireworks.qbit;

import io.advantageous.qbit.annotation.RequestMapping;

@RequestMapping("/hello")
public class HelloWorldService {

	@RequestMapping("/hello")
	public String hello() {
		return "hello qbit in 8088 " + System.currentTimeMillis();
	}

}