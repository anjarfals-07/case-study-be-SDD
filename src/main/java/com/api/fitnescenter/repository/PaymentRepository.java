package com.api.fitnescenter.repository;

import com.api.fitnescenter.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByCandidateIdAndVerifiedIsFalse(Long candidateId);
}