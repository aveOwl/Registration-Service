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

    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmController.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration/confirm/{code:.*}", method = RequestMethod.GET)
    public String confirm(@PathVariable final String code) {
        userService.confirm(code);

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleNoResultException(final NoResultException e) {
        LOG.error(e.getMessage());
        ModelAndView model = new ModelAndView();

        final String desc = "There is no content available. " + e.getMessage();

        model.addObject("code", HttpStatus.BAD_REQUEST.value());
        model.addObject("reason", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addObject("description", desc);
        model.setViewName("error");

        return model;
    }
}
