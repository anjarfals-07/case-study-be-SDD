package com.api.fitnescenter.controller;

import com.api.fitnescenter.dto.SubscriptionRequest;
import com.api.fitnescenter.entity.Subscription;
import com.api.fitnescenter.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    @PostMapping("/subscribe")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Subscription> saveSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
        Subscription subscription = subscriptionService.saveSubscription(subscriptionRequest);
        if (subscription != null) {
            return ResponseEntity.ok(subscription);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/{subscriptionId}/subscriptions")
    public ResponseEntity<Subscription> getSubscriptionDetails(@PathVariable Long subscriptionId) {
        Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
        if (subscription != null) {
            return ResponseEntity.ok(subscription);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{candidateId}/candidate-subscriptions")
    public ResponseEntity<List<Subscription>> getCandidateSubscriptions(@PathVariable Long candidateId) {
        List<Subscription> subscriptions = subscriptionService.getCandidateSubscriptions(candidateId);
        return ResponseEntity.ok(subscriptions);
    }
    @PutMapping("/{subscriptionId}/add-duration")
    public ResponseEntity<Object> addDurationToSubscription(
            @PathVariable Long subscriptionId,
            @RequestParam(name = "durationInMinutes") int additionalDurationInMinutes) {

        return subscriptionService.addDurationToSubscription(subscriptionId, additionalDurationInMinutes);
    }
    @DeleteMapping("/subscriptions/{subscriptionId}/cancel")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok("Subscription canceled successfully.");
    }
//    @PostMapping("/{subscriptionId}/add-duration")
//    public ResponseEntity<Object> addDurationToSubscription(
//            @PathVariable Long subscriptionId,
//            @RequestParam(name = "durationInMinutes") int additionalDurationInMinutes) {
//
//        return subscriptionService.addDurationToSubscription(subscriptionId, additionalDurationInMinutes);
//    }


//    @PostMapping("/subscriptions/{subscriptionId}/cancel")
//    public ResponseEntity<String> cancelSubscription(@PathVariable Long subscriptionId) {
//        subscriptionService.cancelSubscription(subscriptionId);
//        return ResponseEntity.ok("Subscription canceled successfully.");
//    }

}
