package com.olegf.spingapp.thealthbackend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

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
    private LocalDateTime createdAt;
    private boolean isExpired;
    private int attempts;

    public Otp(String phone, String code) {
        this.phone = phone;
        this.code = code;
        this.createdAt = LocalDateTime.now();
        this.isExpired = false;
        this.attempts = 0;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public boolean checkExpired(TemporalAmount interval) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.createdAt.plus(interval));
    }
}
