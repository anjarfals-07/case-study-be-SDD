package com.api.fitnescenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ListOfServices listOfServices;

    private Integer numberOfSessions;
    private LocalDateTime startDate;
    private LocalDateTime  endDate;
    private boolean active; // Status langganan

}
