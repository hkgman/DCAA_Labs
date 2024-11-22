package com.example.gateway.filter;


import com.example.gateway.error.UnauthorizedException;
import com.example.gateway.model.User;
import com.example.gateway.service.JwtUtil;
import com.example.gateway.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
public class JwtFilter implements GlobalFilter, Ordered {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final String uri = request.getURI().toString();
        if (uri.startsWith("/auth") || uri.contains("swagger") || uri.contains("api-docs") || uri.startsWith("/sort")) {
            return chain.filter(exchange);
        }
        if (!request.getHeaders().containsKey("Authorization")) {
            throw new UnauthorizedException();
        }
        String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
        try {
            if (jwtUtil.isExpired(token)) {
                throw new UnauthorizedException();
            }
        } catch (Exception ex) {
            throw new UnauthorizedException();
        }
        String email = jwtUtil.getSubject(token);
        Optional<User> user = userService.find(email);
        if (user.isPresent()) {
            return chain.filter(exchange);
        } else {
            throw new UnauthorizedException();
        }
    }
    @Override
    public int getOrder() {
        return 0;
    }
}