package com.ms.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.util.UUID.randomUUID;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    private final FilterUtility filterUtility;

    public RequestTraceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var requestHeaders = exchange.getRequest().getHeaders();
        var correlationId = filterUtility.getCorrelationId(requestHeaders);

        if (correlationId != null) {
            logger.debug(
                    "Correlation ID found in RequestTraceFilter: {}",
                    correlationId
            );
        } else {
            correlationId = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            logger.debug(
                    "Correlation ID generated in RequestTraceFilter: {}",
                    correlationId
            );
        }

        return chain.filter(exchange);
    }

    private String generateCorrelationId() {
        return randomUUID().toString();
    }
}
