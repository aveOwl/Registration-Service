package com.registration.controller;

import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;

@Controller
public class ConfirmController {

    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmController.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration/confirm/{encrypt:.*}", method = RequestMethod.GET)
    public String confirm(@PathVariable final String encrypt) {
        final String[] data = new String(Base64Utils.decodeFromString(encrypt)).split(":");
        LOG.info("Decrypted email: {}", data[0]);

        userService.confirm(data[0]);

        return "redirect:/success";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String confirm() {
        return "success";
    }

    /**
     * Handles NoResultException. Renders error page with the Exception
     * detail and a HTTP status code 400, bad request.
     *
     * @param e A NoResultException instance.
     * @return An Error page containing a the Exception message and
     * a HTTP status code 400, bad request.
     */
    @ExceptionHandler(NoResultException.class)
    public ModelAndView handleNoResultException(final NoResultException e) {
        LOG.error("NoResultException: ", e.getMessage());
        ModelAndView model = new ModelAndView("error");
        model.addObject("errorMsg", e.getMessage());
        return model;
    }
}
