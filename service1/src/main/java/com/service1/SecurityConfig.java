package com.service1;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebFluxSecurity
public class SecurityConfig {
	
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		http.csrf()
        .disable()
        .authorizeExchange()
        .matchers(PathRequest.toStaticResources().atCommonLocations())
        .permitAll()
        .matchers(EndpointRequest.to("health"))
        .permitAll()
        .matchers(EndpointRequest.to("info"))
        .permitAll()
				.anyExchange().authenticated()
				.and()
                .oauth2Client()
                .and()
			.oauth2ResourceServer()
				.jwt();
		return http.build();
	}
	
	@Bean
	WebClient webClient(
	  ReactiveClientRegistrationRepository clientRegistrations,
	  ServerOAuth2AuthorizedClientRepository authorizedClients) {
	    ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
	      new ServerOAuth2AuthorizedClientExchangeFilterFunction(
	        clientRegistrations,
	        authorizedClients);
	    oauth.setDefaultOAuth2AuthorizedClient(true);
	    return WebClient.builder()
	      .filter(oauth)
	      .build();
	}

}
