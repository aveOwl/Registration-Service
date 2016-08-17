package com.registration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;

@Controller
public class BaseController {
    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    /**
     * Handles specific NoResultException that is thrown if user attempts to confirm registration
     * with invalid confirmation link.
     *
     * @param e A NoResultException instance.
     * @return An Error page containing the Exception message and
     * a HTTP status code 400, bad request.
     */
    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleNoResultException(final NoResultException e) {
        return getErrorModel(HttpStatus.BAD_REQUEST, e);
    }

    /**
     * Creates error model for error page view.
     * @param status response status for given exception.
     * @param e exception that needs to be handled.
     * @return complete error model for the view.
     */
    private ModelAndView getErrorModel(final HttpStatus status, final Exception e) {
        LOG.error(e.getMessage());

        ModelAndView model = new ModelAndView();

        final String desc = "There is no content available. " + e.getMessage();

        model.addObject("code", status.value());
        model.addObject("reason", status.getReasonPhrase());
        model.addObject("description", desc);
        model.setViewName("error");

        return model;
    }
}
