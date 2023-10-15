package com.api.fitnescenter.dto;

import com.api.fitnescenter.entity.CreditCard;
import lombok.Data;

@Data
public class CandidateRequest {
        private String name;
        private String email;
        private String password;
        private String phoneNumber;
        private CreditCardRequest creditCard;
}
