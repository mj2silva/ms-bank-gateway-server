package com.ms.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class FilterUtility {
    public static final String CORRELATION_ID_HEADER = "X-MsBank-Correlation-Id";

    public String getCorrelationId(HttpHeaders requestHeaders) {
        var requestHeader = requestHeaders.get(CORRELATION_ID_HEADER);
        if (requestHeader != null) {
            return requestHeader.stream().findFirst().orElse(null);
        }
        return null;
    }


    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return exchange.mutate().request(
                exchange.getRequest().mutate().header(CORRELATION_ID_HEADER, correlationId).build()
        ).build();
    }
}
