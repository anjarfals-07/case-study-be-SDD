package com.api.fitnescenter.controller;


import com.api.fitnescenter.dto.PaymentRequest;
import com.api.fitnescenter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/request-verification")
    public ResponseEntity<String> requestPaymentVerification(@RequestBody PaymentRequest paymentRequest) {
        paymentService.requestVerification(paymentRequest);
        return ResponseEntity.ok("Verification OTP sent to your email.");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentRequest paymentRequest) {
        boolean verified = paymentService.verifyPayment(paymentRequest);
        if (verified) {
            return ResponseEntity.ok("Payment verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP or OTP has expired.");
        }
    }
}