package com.vijay.apigateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
	
	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		Function<PredicateSpec, Buildable<Route>> routeFunction
		= p -> p.path("/get")
			.filters(f -> f
					.addRequestParameter("param", "MyValue")
					.addRequestHeader("MyHeader", "MyURI"))
			.uri("http://httpbin.org:80");
		return builder.routes()
				.route(routeFunction)
				.route(p -> p.path("/currency-exchange/**")
						.uri("lb://currency-exchange-service"))
				.route(p -> p.path("/currency-converter/**")
						.uri("lb://currency-conversion-service"))
				.route(p -> p.path("/currency-converter-feign/**")
						.uri("lb://currency-conversion-service"))
				.route(p -> p.path("/currency-converter-new/**")
						.filters(f -> f.rewritePath(
								"currency-converter-new/(?<segment>.*)", 
								"currency-converter-feign/${segment}"))
						.uri("lb://currency-conversion-service"))
				.build();
		
	}

}
