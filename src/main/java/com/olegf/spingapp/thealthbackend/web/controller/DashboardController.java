package com.olegf.spingapp.thealthbackend.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {



    @PostMapping("dashboard/steps")
    public String getDashboardSteps() {
        return;
    }
}
