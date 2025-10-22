package com.olegf.spingapp.thealthbackend.web.controller;

import com.olegf.spingapp.thealthbackend.domain.service.OtpService;
import com.olegf.spingapp.thealthbackend.web.dto.OtpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequestDto otpRequestDto) {
        String code = otpService.generateOtp(otpRequestDto.getPhone());
        System.out.println("OTP for " + otpRequestDto.getPhone() + ": " + code);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequestDto otpRequestDto) {
        boolean valid = otpService.verifyOtp(otpRequestDto.getPhone(), otpRequestDto.getCode());
        if (valid)
            return ResponseEntity.ok("OTP verified");
        return ResponseEntity.badRequest().body("Invalid or expired OTP ");
    }
}
