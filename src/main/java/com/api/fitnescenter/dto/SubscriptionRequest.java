package com.api.fitnescenter.dto;

import com.api.fitnescenter.entity.Candidate;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SubscriptionRequest {
    private Long candidateId;
    private Long serviceId;
    private Integer numberOfSessions;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}