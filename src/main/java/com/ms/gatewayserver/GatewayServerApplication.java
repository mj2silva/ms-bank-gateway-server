package com.ms.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder
				.routes()
				.route(
						predicateSpec -> predicateSpec
								.path("/ms-bank/accounts/**")
								.filters(gatewayFilterSpec -> gatewayFilterSpec
										.rewritePath("/ms-bank/accounts/?(?<segment>.*)", "/${segment}")
										.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
										.circuitBreaker(config -> config
												.setName("accounts-circuit-breaker")
												.setFallbackUri("forward:/contact-support")
										)
								)
								.uri("lb://ACCOUNTS")
				)
				.route(
						predicateSpec -> predicateSpec
								.path("/ms-bank/cards/**")
								.filters(gatewayFilterSpec -> gatewayFilterSpec
										.rewritePath("/ms-bank/cards/?(?<segment>.*)", "/${segment}")
										.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								)
								.uri("lb://CARDS")
				)
				.route(
						predicateSpec -> predicateSpec
								.path("/ms-bank/loans/**")
								.filters(gatewayFilterSpec -> gatewayFilterSpec
										.rewritePath("/ms-bank/loans/?(?<segment>.*)", "/${segment}")
										.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								)
								.uri("lb://LOANS")
				)
				.build();
	}

}
