package com.api.fitnescenter.config;

import com.api.fitnescenter.constant.Role;
import com.api.fitnescenter.entity.Candidate;
import com.api.fitnescenter.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Tambahkan peran "CANDIDATE" jika peran pengguna adalah CANDIDATE
        if (candidate.getRole() == Role.CANDIDATE) {
            authorities.add(new SimpleGrantedAuthority("CANDIDATE"));
        }

        if (candidate.getRole() == Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        // Log peran (roles) pengguna
        System.out.println("User roles: " + authorities);
        return new User(candidate.getEmail(), candidate.getPassword(), authorities);
    }

}
