package com.ms.gatewayserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/contact-support")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Mono<String> contactSupport() {
        return Mono.just(
                "An error has occurred and the service is temporary down. If the problem continues" +
                        ", please contact the technical support team for assistance.");
    }
}
