package com.api.fitnescenter.service;

import com.api.fitnescenter.dto.CandidateRequest;
import com.api.fitnescenter.dto.CreditCardRequest;
import com.api.fitnescenter.entity.Candidate;
import com.api.fitnescenter.entity.CreditCard;
import com.api.fitnescenter.repository.CandidateRepository;
import com.api.fitnescenter.utils.GenerateOtp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private EmailService emailService;

    public CandidateService(CandidateRepository candidateRepository, EmailService emailService) {
        this.candidateRepository = candidateRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Candidate updateCandidate(Long candidateId, CandidateRequest candidateRequest) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        updateCandidateFields(candidate, candidateRequest);
        updateCreditCard(candidate, candidateRequest.getCreditCard());

        return candidateRepository.save(candidate);
    }

//    Update Candidate
    private void updateCandidateFields(Candidate candidate, CandidateRequest candidateRequest) {
        if (candidateRequest.getName() != null && !candidateRequest.getName().isEmpty()) {
            candidate.setName(candidateRequest.getName());
        }

        if (candidateRequest.getPhoneNumber() != null && !candidateRequest.getPhoneNumber().isEmpty()) {
            candidate.setPhoneNumber(candidateRequest.getPhoneNumber());
        }

        if (candidateRequest.getPassword() != null && !candidateRequest.getPassword().isEmpty()) {
            candidate.setPassword(new BCryptPasswordEncoder().encode(candidateRequest.getPassword()));
        }

        String newEmail = candidateRequest.getEmail();
        if (newEmail != null && !newEmail.isEmpty() && !newEmail.equals(candidate.getEmail())) {
            candidate.setEmail(newEmail);
            candidate.setVerified(false);
            String otp = new GenerateOtp().generateOTP();
            candidate.setVerificationCode(otp);
            emailService.sendVerificationEmail(newEmail, otp);
        }
    }

    private void updateCreditCard(Candidate candidate, CreditCardRequest creditCardRequest) {
        if (creditCardRequest != null) {
            CreditCard creditCard = candidate.getCreditCard();
            if (creditCard == null) {
                creditCard = new CreditCard();
                candidate.setCreditCard(creditCard);
            }

            // Memperbarui nomor kartu jika diisi dan mengenkripsinya
            if (creditCardRequest.getCardNumber() != null && !creditCardRequest.getCardNumber().isEmpty()) {
                creditCard.setCardNumber(new BCryptPasswordEncoder().encode(creditCardRequest.getCardNumber()));
            }

            // Memperbarui CVV jika diisi dan mengenkripsinya
            if (creditCardRequest.getCvv() != null && !creditCardRequest.getCvv().isEmpty()) {
                creditCard.setCvv(new BCryptPasswordEncoder().encode(creditCardRequest.getCvv()));
            }

            // Memperbarui tanggal kadaluarsa jika diisi
            if (creditCardRequest.getExpiryDate() != null) {
                creditCard.setExpiryDate(creditCardRequest.getExpiryDate());
            }

            // Memperbarui nama pemegang kartu jika diisi
            if (creditCardRequest.getCardHolderName() != null && !creditCardRequest.getCardHolderName().isEmpty()) {
                creditCard.setCardHolderName(creditCardRequest.getCardHolderName());
            }
        }
    }

}
