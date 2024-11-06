package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.example.gateway.filter.FilterOrderType.POST;

@Component
@Slf4j
public class CorrelationTrackingPostFilter implements GlobalFilter, Ordered {
    public static final String CORRELATION_ID = "correlation-id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String correlationId = getCorrelationId(headers);
        log.info("Injecting correlation id: {}", correlationId);

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header(CORRELATION_ID, correlationId)
                .build();

        ServerWebExchange newExchange = exchange.mutate().request(request).build();

        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return POST.getOrder();
    }

    private String getCorrelationId(HttpHeaders headers) {
        return Objects.requireNonNull(headers.getFirst(CORRELATION_ID));
    }
}