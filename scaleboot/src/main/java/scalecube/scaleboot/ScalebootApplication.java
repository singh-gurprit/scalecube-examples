package scalecube.scaleboot;

import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.gateway.http.HttpGateway;
import io.scalecube.services.gateway.ws.WebsocketGateway;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import io.scalecube.transport.netty.websocket.WebsocketTransportFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties(value = {DataStaxAstraProperties.class})
public class ScalebootApplication {

	public static void main(String[] args) throws InterruptedException {
		var context = SpringApplication.run(ScalebootApplication.class, args);

		var profileService = context.getBean("profileService");
		// ScaleCube Node with no members
		Microservices seed =
				Microservices.builder()
						.discovery(
								serviceEndpoint ->
										new ScalecubeServiceDiscovery()
												.transport(cfg -> cfg.transportFactory(new WebsocketTransportFactory()))
												.options(opts -> opts.metadata(serviceEndpoint)))
						.transport(RSocketServiceTransport::new)
						.startAwait();

		final Address seedAddress = seed.discovery().address();
		System.out.println("Host: " + seedAddress.host() + " Port: " + seedAddress.port());

		// Construct a ScaleCube node which joins the cluster hosting the Greeting Service
		Microservices ms =
				Microservices.builder()
						.discovery(
								endpoint ->
										new ScalecubeServiceDiscovery()
												.transport(cfg -> cfg.transportFactory(new WebsocketTransportFactory()))
												.options(opts -> opts.metadata(endpoint))
												.membership(cfg -> cfg.seedMembers(seedAddress)))
						.transport(RSocketServiceTransport::new)
						.services(profileService)
						.gateway(options -> new WebsocketGateway(options.id("ws").port(7071)))
						.gateway(options -> new HttpGateway(options.id("http").port(7072)))
						.startAwait();


//        // Create service proxy
//        BidiGreetingService service = seed.call().api(BidiGreetingService.class);
//
//        // Execute the services and subscribe to service events
//        service
//                .greeting(Flux.fromArray(new String[] {"joe", "dan", "roni"}))
//                .doOnNext(System.out::println)
//                .blockLast();
		Thread.currentThread().join();
	}

	@Bean
	ApplicationListener<ApplicationReadyEvent> ready(ProfileRepository repository) {
		return event -> {
			System.out.println("Hello Scalecube/Cassandra with Spring!");
			var delete = repository.deleteAll();

			var writes = Flux.just("Sonali", "Gotham", "Vaishnavi", "Ayman")
					.flatMap(name -> repository.save(new Profile(UUID.randomUUID(), name)));

			var all = repository.findAll();

			delete.thenMany(writes)
					.thenMany(all.doOnNext(p -> System.out.println(p)))
					.subscribe();

		};
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties dataStaxAstraProperties) {
		Path bundle = dataStaxAstraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
}
