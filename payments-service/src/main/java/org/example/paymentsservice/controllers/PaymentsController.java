package org.example.paymentsservice.controllers;

import org.example.paymentsservice.db_entities.Account;
import org.example.paymentsservice.services.PaymentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentsController {

    private final PaymentsService service;

    public PaymentsController(PaymentsService service) {
        this.service = service;
    }


    @PostMapping(value = "/{id}")
    public ResponseEntity<?> createAccount(@PathVariable("id") long id) {
        try {
            service.createAccount(id);
            return ResponseEntity.ok(service.getAccountById(id));
        } catch (PaymentsService.AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error" + ex.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAccount(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(service.getAccountById(id));
        } catch (PaymentsService.AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error" + ex.getMessage());
        }
    }

    @PostMapping(value = "/{id}/top-up")
    public ResponseEntity<?> topUpAccount(@PathVariable("id") long id, @RequestParam("amount") double amount) {
        try {
            Account account = service.getAccountById(id);
            service.changBalance(id, account.getBalance() + amount);
            return ResponseEntity.ok(service.getAccountById(id));
        } catch (PaymentsService.AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid amount" + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error" + ex.getMessage());
        }
    }
}
