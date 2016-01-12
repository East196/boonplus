package cn.fireworks.qbit;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.annotation.RequestMapping;

@RequestMapping("/hello")
public class HelloWorldService {

	@RequestMapping("/hello")
	public String hello() {
		return "hello qbit in 8088 " + System.currentTimeMillis();
	}

	public static void main(final String... args) {
		final ManagedServiceBuilder managedServiceBuilder = ManagedServiceBuilder.managedServiceBuilder().setPort(8088)
				.setRootURI("/root");

		/* Start the service. */
		managedServiceBuilder.addEndpointService(new HelloWorldService()).getEndpointServerBuilder().build()
				.startServer();
		/* Start the admin builder which exposes health end-points and meta data. */
		managedServiceBuilder.getAdminBuilder().build().startServer();

		System.out.println("Servers started");
	}

}