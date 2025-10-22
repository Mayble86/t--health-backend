package com.olegf.spingapp.thealthbackend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Base64;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String code;
    private LocalDateTime expiresAt;
    private boolean used;
    private int attempts;

    @PrePersist
    @PreUpdate
    private void encryptoPhone() {
        if (phone != null) {
            phone = Base64.getEncoder().encodeToString(phone.getBytes());
        }
    }

    @PostLoad
    private void decryptoPhone() {
        if (phone != null) {
            phone = new String(Base64.getDecoder().decode(phone));
        }
    }
}
