package org.example.myapigateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api-gateway")
public class FileGatewayController {

    private final RestTemplate rest;

    @Autowired
    public FileGatewayController(RestTemplate rest) {
        this.rest = rest;
    }

    @Operation(
            summary = "Получить информацию о заказе по айди"
    )
    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") long id) {
        String url = "lb://orders-service/order/" + id;
        try {
            ResponseEntity<String> response = rest.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Orders service is unavailable");
        }
    }

    @Operation(
            summary = "Получить информацию о всех заказах"
    )
    @GetMapping(value = "/orders/all")
    public ResponseEntity<?> getAllOrder() {
        String url = "lb://orders-service/order/all";
        try {
            ResponseEntity<String> response = rest.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Orders service is unavailable");
        }
    }

    @Operation(
            summary = "Создать заказ"
    )
    @PostMapping(value = "/orders")
    public ResponseEntity<?> createOrder(
            @RequestParam("userId") long userId,
            @RequestParam("sum") double sum,
            @RequestParam("name") String name
    ) {
        String url = UriComponentsBuilder
                .fromUriString("lb://orders-service/order/create")
                .queryParam("userId", userId)
                .queryParam("sum", sum)
                .queryParam("name", name)
                .toUriString();
        try {
            ResponseEntity<String> response = rest.exchange(
                    url,
                    HttpMethod.POST,
                    null,
                    String.class
            );
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Orders service is unavailable");
        }
    }

    @Operation(
            summary = "Получить информацию о счете по айди"
    )
    @GetMapping(value = "/payments/{id}")
    public ResponseEntity<?> getAccount(@PathVariable("id") long id) {
        String url = "lb://payments-service/payment/" + id;
        try {
            ResponseEntity<String> response = rest.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Payments service is unavailable");
        }
    }

    @Operation(
            summary = "Создать счет"
    )
    @PostMapping(value = "/payments/{id}")
    public ResponseEntity<?> createAccount(@PathVariable("id") long id) {
        String url = "lb://payments-service/payment/" + id;
        try {
            ResponseEntity<String> response = rest.exchange(
                    url,
                    HttpMethod.POST,
                    null,
                    String.class
            );
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Payments service is unavailable");
        }
    }

    @Operation(
            summary = "Пополнить счет"
    )
    @PostMapping("/payments/{id}/top-up")
    public ResponseEntity<?> topUpAccount(
            @PathVariable("id") long id,
            @RequestParam("amount") double amount) {

        String url = UriComponentsBuilder
                .fromUriString("lb://payments-service/payment/{id}/top-up")
                .queryParam("amount", amount)
                .buildAndExpand(id)
                .toUriString();

        try {
            ResponseEntity<String> response = rest.exchange(
                    url,
                    HttpMethod.POST,
                    null,
                    String.class
            );
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (ResourceAccessException ex) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Payments service is unavailable");
        }
    }
}
