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
import java.util.UUID;

import static com.olegf.spingapp.thealthbackend.validator.PhoneValidator.validate;

@Service
@Slf4j
@AllArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;

    public Otp handleOtp(String phone) {
        try {
            validate(phone);
            Otp otp = otpRepository.existsByPhone(phone)
                    ? otpRepository.findByPhone(phone).orElseThrow(() -> new RuntimeException("Entity not found From DB!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"))
                    : new Otp(phone, UUID.randomUUID().toString()); //else

            log.debug("OTP for {}", otp);
            return otpRepository.save(otp);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public boolean verify(String otpCode, String phone) {
        return otpRepository.findByPhone(phone).orElse(false).stream().anyMatch(it -> {
            it == otpCode;
        });
    }
}
