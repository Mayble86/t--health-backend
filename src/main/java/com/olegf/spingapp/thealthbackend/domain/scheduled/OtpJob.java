package com.olegf.spingapp.thealthbackend.domain.scheduled;

import com.olegf.spingapp.thealthbackend.domain.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtpJob {
    private final OtpService otpService;

    @Scheduled(fixedRate = 1000 * 60)
    public void deleteCods() {
        otpService.handleExpired();
    }
}
