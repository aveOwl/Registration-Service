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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegisterController {
    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    private UserService userService;
    private MailService mailService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect() {
        LOG.info("Redirecting to home page...");
        return "redirect:/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView home(User user) {
        LOG.info("Rendering home page...");
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public ValidationResult register(final @Valid User user,
                                     final BindingResult bindingResult,
                                     final HttpServletRequest request) {
        LOG.info("Attempting user registration...");

        if (!bindingResult.hasErrors()) {
            userService.create(user);
            mailService.sendMail(user, request);
        } else {
            LOG.error("User verification failed: {}", bindingResult.getFieldErrors());
        }
        return new ValidationResult(bindingResult);
    }
}
