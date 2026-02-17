package com.smartlease.smartlease_backend.controller;

import com.razorpay.RazorpayException;
import com.smartlease.smartlease_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order/{amount}")
    public ResponseEntity<String> createOrder(@PathVariable Double amount){
            String respone = paymentService.createOrder(amount);
            return ResponseEntity.ok(respone);
    }

}
