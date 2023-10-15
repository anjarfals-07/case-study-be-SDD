package com.api.fitnescenter.service;

import com.api.fitnescenter.constant.Role;
import com.api.fitnescenter.dto.CandidateRequest;
import com.api.fitnescenter.dto.CreditCardRequest;
import com.api.fitnescenter.entity.Candidate;
import com.api.fitnescenter.entity.CreditCard;
import com.api.fitnescenter.repository.CandidateRepository;
import com.api.fitnescenter.service.EmailService;
import com.api.fitnescenter.utils.GenerateOtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RegistrationService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private EmailService emailService;

    public Candidate registerCandidate(CandidateRequest candidateRequest) {
        // Chek Email apakah Sudah Digunakan
        Optional<Candidate> existingCandidate = candidateRepository.findByEmail(candidateRequest.getEmail());
        if (existingCandidate.isPresent()) {
            throw new RuntimeException("Email is already in use");
        }
        // Generate OTP
        GenerateOtp generateOtp = new GenerateOtp();
        String otp = generateOtp.generateOTP();

        // Send verification email with OTP
        emailService.sendVerificationEmail(candidateRequest.getEmail(), otp);

        // Save candidate
        Candidate candidate = new Candidate();
        candidate.setName(candidateRequest.getName());
        candidate.setEmail(candidateRequest.getEmail());
        candidate.setPassword(new BCryptPasswordEncoder().encode(candidateRequest.getPassword()));
        candidate.setPhoneNumber(candidateRequest.getPhoneNumber());
        candidate.setVerificationCode(otp); // Set verification code
        candidate.setRole(Role.CANDIDATE);
        CreditCardRequest creditCardRequest = candidateRequest.getCreditCard();

        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(creditCardRequest.getCardNumber());
        creditCard.setCvv(creditCardRequest.getCvv());
        creditCard.setExpiryDate(creditCardRequest.getExpiryDate());
        creditCard.setCardHolderName(creditCardRequest.getCardHolderName());

        candidate.setCreditCard(creditCard);

        candidateRepository.save(candidate);

        return candidate;
    }

}