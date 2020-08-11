package com.service1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/index1")
@SpringBootApplication
@EnableDiscoveryClient
public class Service1Application {
	
	@Autowired
	private WebClient webClient;

	public static void main(String[] args) {
        SpringApplication.run(Service1Application.class, args);
    }
	
	@GetMapping
	public Mono<String> index( @AuthenticationPrincipal Jwt jwt,ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
		 Mono<String> retrievedResource = webClient.get()
			      .uri("http://localhost:8089/service2/index2")
			      .header("Authorization", headers.get("Authorization").get(0))
			      .retrieve()
			      .bodyToMono(String.class);


		return retrievedResource;
	}

	
	

	
}
