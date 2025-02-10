package com.demo.filter;

import com.demo.dto.Authorities;
import com.demo.dto.ExceptionResponseModel;
import com.demo.dto.TokenValidationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config> {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationPrefilter.class);
    private final WebClient.Builder webClient;
    private ObjectMapper objectMapper;
    private List<String > excludedUrls = List.of("/api/v1/validConnection/register", "/api/v1/validConnection/login");

    public AuthenticationPrefilter(WebClient.Builder webClient, ObjectMapper objectMapper) {
        super(Config.class);
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getHeaders().getFirst("Authorization");

            log.info("************************************");
            log.info("URL : {}", request.getPath().value());
            log.info("Token is : {}", token);

            if(isSecured.test(request)){
                return webClient.build().get()
                        .uri("http://localhost:8083/api/v1/validateToken") //Create a non-blocking Http Client for given url
                        .header("Authorization", token) //Set Authorization token in the header of client request
                        .retrieve()// send request and receive the response
                        .bodyToMono(TokenValidationResponse.class)// Mapping the response in the TokenValidationResponse class
                        .switchIfEmpty(Mono.error(new RuntimeException("Empty response from authentication service")))
                        .flatMap(response->{

                            //Creates a mutated copy of the original request
                            //Add userName and authority in the header
                            //Creates the modified ServerHttpRequest object.
                            ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                                    .header("userName", response.getUsername())
                                    .header("authority", response.getAuthorities()
                                            .stream().map(Authorities::getAuthority).collect(Collectors.joining(",")))
                                    .build();
                            //Create a mutate copy of current exchange
                            //And replace the request with new httpRequest
                            //Creates the Updated ServerWebExchange object.
                            ServerWebExchange mutateExchange = exchange.mutate().request(httpRequest).build();
                            return  chain.filter(mutateExchange); //forward to the next server
                        }).onErrorResume(error->{
                            log.error("Error during validating the token: {}", error.getMessage(), error);
                            HttpStatusCode errorCode = error instanceof WebClientResponseException
                                    ? ((WebClientResponseException) error).getStatusCode(): HttpStatus.UNAUTHORIZED;
                            return onError(exchange, String.valueOf(errorCode), "JWT Authentication failed", errorCode);
                        });

            }

            //bypass the excluded urls
            return chain.filter(exchange);

        };


    }

    //Return false if un-match url request contain
    private final Predicate<ServerHttpRequest> isSecured = request -> excludedUrls.stream().noneMatch(uri-> request.getPath().value().contains(uri));

    private Mono<Void> onError(ServerWebExchange exchange, String errCode,
                               String errorMessage, HttpStatusCode httpStatusCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatusCode);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        try {
            ExceptionResponseModel data = new ExceptionResponseModel(errCode, errorMessage, new Date());
            byte[] byteData = objectMapper.writeValueAsBytes(data);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(byteData)));
        } catch (JsonProcessingException e) {
            log.error("Error serializing exception response", e);
            return response.setComplete();
        }
    }

    public static class Config{

    }

}
