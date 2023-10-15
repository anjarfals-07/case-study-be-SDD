    package com.api.fitnescenter.dto;

    import lombok.Data;

    import java.time.LocalDate;

    @Data
    public class CreditCardRequest {
        private String cardNumber;
        private String cvv;
        private LocalDate expiryDate;
        private String cardHolderName;

    }
