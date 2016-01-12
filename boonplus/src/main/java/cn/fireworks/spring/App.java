package cn.fireworks.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.fireworks.qbit.HelloWorldService;
import io.advantageous.qbit.admin.ManagedServiceBuilder;

@SpringBootApplication
@RestController
public class App {

	@RequestMapping("/root/hello/hello")
	public String hello() {
		return "hello spring in 8089 " + System.currentTimeMillis();
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		final ManagedServiceBuilder managedServiceBuilder = ManagedServiceBuilder.managedServiceBuilder().setPort(8088)
				.setRootURI("/root");

		managedServiceBuilder.addEndpointService(new HelloWorldService()).getEndpointServerBuilder().build()
				.startServer();
	}

}
