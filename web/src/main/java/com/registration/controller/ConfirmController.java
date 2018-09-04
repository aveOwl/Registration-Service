package com.registration.controller;

import com.registration.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ConfirmController {
    private final UserService userService;

    @GetMapping("/registration/confirm/{code:.*}")
    public String confirm(@PathVariable String code) {
        log.info("Attempting user confirmation...");
        this.userService.confirm(code);
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String confirm() {
        log.info("Rendering success page...");
        return "success-page";
    }
}
