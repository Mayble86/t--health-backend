package com.olegf.spingapp.thealthbackend.web.controller;

import com.olegf.spingapp.thealthbackend.domain.entity.Otp;
import com.olegf.spingapp.thealthbackend.domain.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @GetMapping
    public Otp sendOtp(@RequestParam String phone) {
        return otpService.handleOtp(phone);
    }

    @GetMapping("/verify")
    public void verifyOtp(@RequestParam String otpCode, @RequestParam String phone) {
        otpService.verify(otpCode, phone);
    }
}
