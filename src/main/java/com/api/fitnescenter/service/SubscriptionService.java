package com.api.fitnescenter.service;

import com.api.fitnescenter.dto.SubscriptionRequest;
import com.api.fitnescenter.entity.Candidate;
import com.api.fitnescenter.entity.ListOfServices;
import com.api.fitnescenter.entity.Subscription;
import com.api.fitnescenter.repository.CandidateRepository;
import com.api.fitnescenter.repository.FitnessServicesRepository;
import com.api.fitnescenter.repository.SubscriptionRepository;
import org.hibernate.validator.internal.util.logging.Log;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private FitnessServicesRepository fitnessServicesRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, CandidateRepository candidateRepository, FitnessServicesRepository fitnessServicesRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.candidateRepository = candidateRepository;
        this.fitnessServicesRepository = fitnessServicesRepository;
    }

    public Subscription saveSubscription(SubscriptionRequest subscriptionRequest) {
        // Implementasi pembuatan langganan
        Candidate candidate = candidateRepository.findById(subscriptionRequest.getCandidateId()).orElse(null);
        ListOfServices fitnessService = fitnessServicesRepository.findById(subscriptionRequest.getServiceId()).orElse(null);

        if (candidate != null && fitnessService != null) {
            Subscription subscription = new Subscription();
            subscription.setCandidate(candidate);
            subscription.setListOfServices(fitnessService);
            subscription.setNumberOfSessions(subscriptionRequest.getNumberOfSessions());
            subscription.setStartDate(subscriptionRequest.getStartDate());
            subscription.setEndDate(subscriptionRequest.getEndDate());
            subscription.setActive(true);

            return subscriptionRepository.save(subscription);
        } else {
            return null;
        }
    }

    public List<Subscription> getCandidateSubscriptions(Long candidateId) {
        // Mendapatkan langganan peserta berdasarkan ID kandidat dari repository
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate != null) {
            return candidate.getSubscriptions();
        } else {
            return new ArrayList<>();
        }
    }

    public Subscription getSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId).orElse(null);
    }

    public ResponseEntity<Object> addDurationToSubscription(Long subscriptionId, int additionalDurationInMinutes) {
        Subscription subscription = getSubscriptionById(subscriptionId);
        if (subscription != null) {
            if (subscription.isActive()) {
                int currentDuration = subscription.getListOfServices().getDuration();
                subscription.getListOfServices().setDuration(currentDuration + additionalDurationInMinutes);
                subscription.setNumberOfSessions(subscription.getNumberOfSessions() + 1);
                subscription.setEndDate(subscription.getEndDate().plusMinutes(additionalDurationInMinutes));
                subscriptionRepository.save(subscription);
                return ResponseEntity.ok(subscription);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add duration to inactive subscription.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscription not found.");
        }
    }
    public void cancelSubscription(Long subscriptionId) {
        // Membatalkan langganan berdasarkan ID langganan
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        if (subscription != null) {
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
        }
    }
}

