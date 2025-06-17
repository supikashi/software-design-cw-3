package org.example.ordersservice.controllers;

import org.example.ordersservice.services.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrdersController {
    private final OrdersService service;

    public OrdersController(OrdersService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestParam("userId") long userId,
            @RequestParam("sum") double sum,
            @RequestParam("name") String name
    ) {
        try {
            return ResponseEntity.ok(service.createOrder(userId, sum, name));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error" + ex.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(service.getOrder(id));
        } catch (OrdersService.OrderNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error" + ex.getMessage());
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(service.getAllOrders());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error" + ex.getMessage());
        }
    }
}
