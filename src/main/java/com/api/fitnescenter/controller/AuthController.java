    package com.api.fitnescenter.controller;

    import com.api.fitnescenter.dto.LoginRequest;
    import com.api.fitnescenter.entity.Candidate;
    import com.api.fitnescenter.repository.CandidateRepository;
    import com.api.fitnescenter.service.EmailService;
    import com.api.fitnescenter.service.RegistrationService;
    import com.api.fitnescenter.utils.JwtTokenProvider;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
    import org.springframework.web.bind.annotation.*;

    import java.util.Optional;
    import java.util.UUID;


    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        @Autowired
        private RegistrationService registrationService;

        @Autowired
        private CandidateRepository candidateRepository;

        @Autowired
        private EmailService emailService;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtTokenProvider jwtTokenProvider;


        @Autowired
        private UserDetailsService userDetailsService;

        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
            try {
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                loginRequest.getEmail(),
//                                loginRequest.getPassword().
//                        )
//                );
                // Authentication successful, generate JWT token and return it
                UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
                String token = jwtTokenProvider.generateToken(userDetails);
                return new ResponseEntity<>(token, HttpStatus.OK);
            } catch (AuthenticationException e) {
                // Authentication failed, return an error response
                return new ResponseEntity<>("Login failed: incorrect email or password", HttpStatus.UNAUTHORIZED);
            }
        }

//        @PostMapping("/login")
//        public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//            try {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
//                // Pastikan peran 'CANDIDATE' ditambahkan ke pengguna
//                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.CANDIDATE.toString()));
//                // Tambahkan peran ke pengguna
//                userDetails = new User(userDetails.getUsername(), userDetails.getPassword(), authorities);
//                String token = jwtTokenProvider.generateToken(userDetails);
//                return new ResponseEntity<>(token, HttpStatus.OK);
//            } catch (AuthenticationException e) {
//                return new ResponseEntity<>("Login gagal: email atau password salah", HttpStatus.UNAUTHORIZED);
//            }
//        }

        @PostMapping("/verify-reset-password")
        public ResponseEntity<String> resetPassword(@RequestParam("email") String email) {
            // Generate OTP atau token reset password
            String resetToken = generateResetToken();
            //  reset token candidate
            Optional<Candidate> candidateOptional = candidateRepository.findByEmail(email);
            if (candidateOptional.isPresent()) {
                Candidate candidate = candidateOptional.get();
                candidate.setResetToken(resetToken);
                candidateRepository.save(candidate);
            }
            //  email verifikasi dengan link reset password
            emailService.sendResetPasswordEmail(email, resetToken);
            return new ResponseEntity<>("Email verifikasi reset password telah dikirim", HttpStatus.OK);
        }

        @PostMapping("/reset-password")
        public ResponseEntity<String> verifyResetToken(@RequestParam("email") String email, @RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
            // Verifikasi token
            if (isValidResetToken(email, token)) {
                // Jika Token valid, dapat ganti sandinya
                Optional<Candidate> candidateOptional = candidateRepository.findByEmail(email);
                if (candidateOptional.isPresent()) {
                    Candidate candidate = candidateOptional.get();
                    // Enkripsi kata sandi baru
                    candidate.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                    candidateRepository.save(candidate);
                    return new ResponseEntity<>("Kata sandi telah diubah", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Kandidat tidak ditemukan", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("Token reset password tidak valid", HttpStatus.BAD_REQUEST);
            }
        }
        @PostMapping("/logout")
        public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
            // Ambil token JWT
            String token = jwtTokenProvider.resolveToken(request);
            if (token != null) {
                //hapus token dari daftar token yang aktif
                jwtTokenProvider.invalidateToken(token);
            }
            //logout
            new SecurityContextLogoutHandler().logout(request, null, null);
            return new ResponseEntity<>("Logout berhasil", HttpStatus.OK);
        }
        private String generateResetToken() {
            //token unik
            return UUID.randomUUID().toString();
        }
        private boolean isValidResetToken(String email, String token) {
            // Memeriksa token database
            Optional<Candidate> candidateOptional = candidateRepository.findByEmail(email);
            if (candidateOptional.isPresent()) {
                Candidate candidate = candidateOptional.get();
                // Memeriksa apakah token cocok dengan token yang tersimpan
                return token.equals(candidate.getResetToken());
            }
            return false;
        }
    }





    //    @PostMapping("/reset-password")
    //    public ResponseEntity<String> resetPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
    //        // Generate OTP atau token reset password
    //        String resetToken = generateResetToken();
    //        // Reset token candidate
    //        Optional<Candidate> candidateOptional = candidateRepository.findByEmail(email);
    //        if (candidateOptional.isPresent()) {
    //            Candidate candidate = candidateOptional.get();
    //            candidate.setResetToken(resetToken);
    //            // Enkripsi password baru
    //            candidate.setPassword(new BCryptPasswordEncoder().encode(password));
    //            candidateRepository.save(candidate);
    //        }
    //        // Email verifikasi dengan link reset password
    //        emailService.sendResetPasswordEmail(email, resetToken);
    //
    //        return new ResponseEntity<>("Password telah direset. Email verifikasi reset password telah dikirim", HttpStatus.OK);
    //    }

    //    @PostMapping("/verify-reset-token")
    //    public ResponseEntity<String> verifyResetToken(@RequestParam("email") String email, @RequestParam("token") String token) {
    //        // Verifikasi token
    //        if (isValidResetToken(email, token)) {
    //            // Jika Token valid, dapat ganti sandinya
    //            return new ResponseEntity<>("Token reset password valid", HttpStatus.OK);
    //        } else {
    //            return new ResponseEntity<>("Token reset password tidak valid", HttpStatus.BAD_REQUEST);
    //        }
    //    }

