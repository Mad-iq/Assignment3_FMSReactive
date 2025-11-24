package com.fms.reactive.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fms.reactive.GlobalExceptionHandler;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @SuppressWarnings("deprecation")
	@Test
    void testHandleRuntimeException() {

        RuntimeException ex = new RuntimeException("Something went wrong");

        Mono<ResponseEntity<Map<String, Object>>> responseMono =
                handler.handleRuntimeException(ex);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertTrue(response.getBody().containsKey("error"));
                    assertEquals("Something went wrong", response.getBody().get("error"));
                })
                .verifyComplete();
    }
    
    @SuppressWarnings("deprecation")
	@Test
    void testHandleValidationErrors() {

        Object target = new Object();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "testObject");
        bindingResult.addError(new FieldError("testObject", "email", "Invalid email format"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        Mono<ResponseEntity<Map<String, Object>>> responseMono =
                handler.handleValidationErrors(ex);
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    Map<String, Object> body = response.getBody();

                    assertTrue(body.containsKey("details"));
                    Map<String, String> details = (Map<String, String>) body.get("details");

                    assertEquals("Invalid email format", details.get("email"));
                })
                .verifyComplete();
    }
}
