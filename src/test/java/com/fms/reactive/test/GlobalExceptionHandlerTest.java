package com.fms.reactive.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.fms.reactive.GlobalExceptionHandler;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleRuntimeException() {

        RuntimeException ex = new RuntimeException("Something went wrong");

        Mono<ResponseEntity<Map<String, Object>>> responseMono =
                handler.handleRuntimeException(ex);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(400, response.getStatusCodeValue());
                    assertTrue(response.getBody().containsKey("error"));
                    assertEquals("Something went wrong", response.getBody().get("error"));
                })
                .verifyComplete();
    }
}
