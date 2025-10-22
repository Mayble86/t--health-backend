package com.olegf.spingapp.thealthbackend.domain.service;

import com.olegf.spingapp.thealthbackend.domain.entity.Otp;
import com.olegf.spingapp.thealthbackend.domain.repository.OtpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final RateLimitService rateLimitService;

    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final int OTP_REQUEST_INTERVAL_SECONDS = 60;
    private static final int MAX_ATTEMPTS = 3;

    public String generateOtp(String phone) {
        String encryptedPhone = Base64.getEncoder().encodeToString(phone.getBytes());

        if (!rateLimitService.allowRequest(phone)) {
            log.warn("OTP request limit exceeded for {}", phone);
            throw new IllegalStateException("Слишком много запросов. Попробуйте позже.");
        }

        otpRepository.findByPhone(encryptedPhone).ifPresent(existing -> {
            if (existing.getExpiresAt().isAfter(LocalDateTime.now().minusSeconds(OTP_REQUEST_INTERVAL_SECONDS))) {
                throw new IllegalStateException("код запрашивается cлишком часто");
            }
        });

        String code = String.format("%06d", new Random().nextInt(1_000_000));
        String hashed = BCrypt.hashpw(code, BCrypt.gensalt());

        Otp otp = Otp.builder()
                .phone(phone)
                .code(hashed)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES))
                .used(false)
                .build();

        otpRepository.save(otp);
        log.info("OTP generated for {}", phone);
        return code;
    }

    public boolean verifyOtp(String phone, String code) {
        String encryptedPhone = Base64.getEncoder().encodeToString(phone.getBytes());

        return otpRepository.findByPhone(encryptedPhone)
                .filter(o -> !o.isUsed() && o.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(o -> {
                    boolean valid = BCrypt.checkpw(code, o.getCode());
                    if (valid) {
                        o.setUsed(true);
                        log.info("OTP validated for {}", phone);
                    } else {
                        o.setAttempts(o.getAttempts() + 1);
                        if (o.getAttempts() >= MAX_ATTEMPTS) {
                            o.setUsed(true);
                            log.warn("OTP locked due to max attempts for {}", phone);
                        }
                    }
                    otpRepository.save(o);
                    return valid;
                })
                .orElse(false);
    }

    @Scheduled(fixedDelay = 60000)
    public void cleanExpiredOtps() {
        otpRepository.findAll().forEach(o -> {
            if (o.isUsed() || o.getExpiresAt().isBefore(LocalDateTime.now())) {
                otpRepository.delete(o);
                log.info("Expired OTP deleted for {}", o.getPhone());
            }
        });
    }
}
