package com.registration.controller;

import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ConfirmController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmController.class);

    private UserService userService;

    @Autowired
    public ConfirmController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration/confirm/{code:.*}")
    public String confirm(@PathVariable String code) {
        LOG.info("Attempting user confirmation...");

        this.userService.confirm(code);

        return "redirect:/success";
    }

    @GetMapping("/success")
    public String confirm() {
        LOG.info("Rendering success page...");
        return "success-page";
    }
}
