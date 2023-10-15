package com.api.fitnescenter.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long candidateId;
    private double amount;
    private String otp;
}
