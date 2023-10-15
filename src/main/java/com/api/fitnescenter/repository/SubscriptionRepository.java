package com.api.fitnescenter.repository;

import com.api.fitnescenter.entity.Candidate;
import com.api.fitnescenter.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCandidate(Candidate candidate);
}