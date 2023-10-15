package com.api.fitnescenter.controller;

import com.api.fitnescenter.dto.CandidateRequest;
import com.api.fitnescenter.entity.Candidate;
import com.api.fitnescenter.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PutMapping("/update/{candidateId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public Candidate updateCandidate(@PathVariable Long candidateId, @RequestBody CandidateRequest candidateRequest) {
        return candidateService.updateCandidate(candidateId, candidateRequest);
    }
}