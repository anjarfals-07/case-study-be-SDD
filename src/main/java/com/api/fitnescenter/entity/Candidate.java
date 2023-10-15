package com.api.fitnescenter.entity;

import com.api.fitnescenter.constant.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    private CreditCard creditCard;
    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verified")
    private boolean verified;
    private String resetToken;
    @OneToMany(mappedBy = "candidate")
    private List<Subscription> subscriptions;

}
