package com.registration.controller;

import com.registration.model.User;
import com.registration.service.MailService;
import com.registration.service.UserService;
import com.registration.util.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.registration.Points.DUPLICATE_EMAIL_MSG;

@Controller
public class RegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    private UserService userService;
    private MailService mailService;

    @Autowired
    public RegisterController(final UserService userService,
                              final MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect() {
        LOG.info("Redirecting to home page...");
        return "redirect:/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView home() {
        LOG.info("Rendering home page...");
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public ValidationResult register(final @Valid @RequestBody User user,
                                     final BindingResult bindingResult) {
        LOG.info("Attempting user registration...");

        if (!bindingResult.hasErrors() &&
                this.userService.findByEmail(user.getEmail()).isPresent()) {
            bindingResult.addError(new FieldError("user", "email", DUPLICATE_EMAIL_MSG));
        }

        if (!bindingResult.hasErrors()) {
            LOG.debug("Input: email = {}, password = {} is verified",
                    user.getEmail(), user.getPassword());
            this.userService.create(user);
            this.mailService.sendEmail(user);
        } else {
            LOG.error("Input verification failed: {}", bindingResult.getFieldErrors());
        }
        return new ValidationResult(bindingResult);
    }
}
