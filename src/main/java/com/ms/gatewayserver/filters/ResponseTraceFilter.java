package com.ms.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    private final FilterUtility filterUtility;

    public ResponseTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(
                        Mono.fromRunnable(() -> {
                            var requestHeaders = exchange.getRequest().getHeaders();
                            var correlationId = filterUtility.getCorrelationId(requestHeaders);
                            exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID_HEADER, correlationId);
                            logger.debug("Updated the correlation id to the response headers: {}", correlationId);
                        })
                );
    }
}
