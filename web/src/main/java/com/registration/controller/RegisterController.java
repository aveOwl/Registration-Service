package com.registration.controller;

import com.registration.model.User;
import com.registration.service.MailService;
import com.registration.service.UserService;
import com.registration.util.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.registration.Points.DUPLICATE_EMAIL_MSG;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final MailService mailService;

    @GetMapping("/")
    public String redirect() {
        log.info("Redirecting to home page...");
        return "redirect:/registration";
    }

    @GetMapping("/registration")
    public ModelAndView home() {
        log.info("Rendering home page...");
        return new ModelAndView("index");
    }

    @PostMapping("/registration")
    @ResponseBody
    public ValidationResult register(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.info("Attempting user registration...");

        if (!bindingResult.hasErrors() && this.userService.findByEmail(user.getEmail()) != null) {
            bindingResult.addError(new FieldError("user", "email", DUPLICATE_EMAIL_MSG));
        }

        if (!bindingResult.hasErrors()) {
            log.debug("Input: email = {}, password = {} is verified", user.getEmail(), user.getPassword());
            this.userService.create(user);
            this.mailService.sendEmail(user);
        } else {
            log.error("Input verification failed: {}", bindingResult.getFieldErrors());
        }
        return new ValidationResult(bindingResult);
    }
}
