package com.registration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;

/**
 * <p>
 *     The MainControllerAdvice class provide a consistent response
 *     when Exceptions are thrown from <code>@RequestMapping</code> Controller methods.
 * </p>
 */
@ControllerAdvice
public class MainControllerAdvice {

    /**
     * Handles specific <code>NoResultException</code> that is thrown if user attempts
     * to confirm registration with invalid confirmation link.
     *
     * @param e A NoResultException instance.
     * @return An Error page containing the Exception message and
     * a HTTP status code 400, bad request.
     */
    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleNoResultException(final NoResultException e) {
        return this.getDefaultErrorModel(HttpStatus.BAD_REQUEST, e);
    }

    /**
     * Handles <code>Exception</code> thrown from web service controller methods.
     *
     * @param ex A <code>Exception</code> instance.
     * @return response with HTTP status code 500 and exception message.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exception(final Exception ex) {
        return this.getDefaultErrorModel(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    /**
     * Creates error model for error page view.
     *
     * @param status response status for given exception.
     * @param e exception that needs to be handled.
     * @return complete error model for the view.
     */
    private ModelAndView getDefaultErrorModel(final HttpStatus status, final Exception e) {
        final ModelAndView model = new ModelAndView();

        final String desc = "There is no content available. " + e.getMessage();

        model.addObject("code", status.value());
        model.addObject("reason", status.getReasonPhrase());
        model.addObject("description", desc);
        model.setViewName("error");

        return model;
    }
}
