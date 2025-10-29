package com.olegf.spingapp.thealthbackend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

@Data
@Entity(name = "otps")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ColumnTransformer(
            read = "pgp_sym_decrypt(phone, 'secret')",
            write = "pgp_sym_encrypt(?::text, 'secret')"
    )
    private byte[] phone;
    @ColumnTransformer(
            read = "pgp_sym_decrypt(code, 'secret')",
            write = "pgp_sym_encrypt(?::text, 'secret')"
    )
    private byte[] code;
    private LocalDateTime createdAt;
    private boolean isExpired;
    private int attempts;

    public Otp(String phone, String code) {
        this.phone = phone.getBytes();
        this.code = code.getBytes();
        this.createdAt = LocalDateTime.now();
        this.isExpired = false;
        this.attempts = 0;
    }

    public String getPhone() {
        return new String(phone, StandardCharsets.UTF_8);
    }

    public String getCode() {
        return new String(code, StandardCharsets.UTF_8);
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public boolean checkExpired(TemporalAmount interval) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.createdAt.plus(interval));
    }
}
