    package com.api.fitnescenter.controller;

    import com.api.fitnescenter.dto.CandidateRequest;
    import com.api.fitnescenter.entity.Candidate;
    import com.api.fitnescenter.repository.CandidateRepository;
    import com.api.fitnescenter.service.RegistrationService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/registration")
    public class RegistrationController {
        @Autowired
        private RegistrationService candidateService;
        @Autowired
        private CandidateRepository candidateRepository;
        @PostMapping("/register")
        public ResponseEntity<Candidate> registerCandidate(@RequestBody CandidateRequest candidateRequest) {
            Candidate registeredCandidate = candidateService.registerCandidate(candidateRequest);
            return new ResponseEntity<>(registeredCandidate, HttpStatus.CREATED);
        }
        @PostMapping("/verify-email")
        public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("otp") String otp) {
            // Cari berdasarkan email
            Candidate candidate = candidateRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));
            if (candidate.isVerified()) {
                return new ResponseEntity<>("Calon peserta sudah terverifikasi sebelumnya", HttpStatus.BAD_REQUEST);
            }
            if (otp.equals(candidate.getVerificationCode())) {
                // update status verifikasi
                candidate.setVerified(true);
                candidateRepository.save(candidate);
                return new ResponseEntity<>("Email verifikasi berhasil", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Email verifikasi gagal", HttpStatus.BAD_REQUEST);
            }
        }
        @GetMapping("/check-status")
        public ResponseEntity<String> checkStatus(@RequestParam("email") String email) {
            // Cari berdasarkan email
            Candidate candidate = candidateRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            if (candidate.isVerified()) {
                return new ResponseEntity<>("Status: TERDAFTAR", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Status: BELUM TERDAFTAR", HttpStatus.OK);
            }
        }
    }