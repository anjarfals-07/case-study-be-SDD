package com.api.fitnescenter.service;

import com.api.fitnescenter.dto.PaymentRequest;
import com.api.fitnescenter.entity.Candidate;
import com.api.fitnescenter.entity.Payment;
import com.api.fitnescenter.repository.CandidateRepository;
import com.api.fitnescenter.repository.PaymentRepository;
import com.api.fitnescenter.utils.GenerateOtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CandidateRepository candidateRepository;
    private final EmailService emailService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, CandidateRepository candidateRepository, EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.candidateRepository = candidateRepository;
        this.emailService = emailService;
    }

    public void requestVerification(PaymentRequest paymentRequest) {
        Candidate candidate = candidateRepository.findById(paymentRequest.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + paymentRequest.getCandidateId()));

        // Generate OTP
        GenerateOtp generateOtp = new GenerateOtp();
        String otp = generateOtp.generateOTP();

        // Menyimpan OTP dan waktu pembuatan
        Payment payment = new Payment();
        payment.setCandidate(candidate);
        payment.setAmount(paymentRequest.getAmount());
        payment.setVerified(false);
        payment.setOtp(otp);
        payment.setOtpCreationTime(LocalDateTime.now());

        paymentRepository.save(payment);

        // Mengirimkan OTP ke email peserta
        emailService.sendVerificationEmail(candidate.getEmail(), otp);
    }

    public boolean verifyPayment(PaymentRequest paymentRequest) {
        Payment payment = paymentRepository.findByCandidateIdAndVerifiedIsFalse(paymentRequest.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Payment verification request not found for candidate with id: " + paymentRequest.getCandidateId()));

        if (payment.getOtp().equals(paymentRequest.getOtp()) && !isOtpExpired(payment)) {
            payment.setVerified(true);
            paymentRepository.save(payment);
            return true;
        }
        return false;
    }

    private boolean isOtpExpired(Payment payment) {
        LocalDateTime otpCreationTime = payment.getOtpCreationTime();
        LocalDateTime currentTime = LocalDateTime.now();
        return otpCreationTime.plusMinutes(5).isBefore(currentTime);
    }

//    private String generateRandomOtp() {
//        int otpLength = 6;
//        String numbers = "0123456789";
//        Random random = new Random();
//        StringBuilder otp = new StringBuilder(otpLength);
//        for (int i = 0; i < otpLength; i++) {
//            otp.append(numbers.charAt(random.nextInt(numbers.length())));
//        }
//        return otp.toString();
//    }
}