package com.registration.controller;

import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ConfirmController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmController.class);

    private UserService userService;

    @Autowired
    public ConfirmController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration/confirm/{code:.*}", method = RequestMethod.GET)
    public String confirm(@PathVariable final String code) {
        LOG.info("Attempting user confirmation...");

        this.userService.confirm(code);

        return "redirect:/success";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String confirm() {
        LOG.info("Rendering success page...");
        return "success-page";
    }
}
