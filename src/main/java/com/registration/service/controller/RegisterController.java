package com.registration.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Manages all incoming requests.
 */
@Controller
@EnableAutoConfiguration
public class RegisterController {

    /**
     * Logging system.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    /**
     * Redirects request on root path to the home page.
     * @return redirecting path to the home page.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect() {
        LOG.info("redirecting to home page");
        return "redirect:/registration";
    }

    /**
     * Displays home page.
     * @return home page model.
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView showHomePage() {
        LOG.info("displaying home page");
        return getRegisterModel();
    }

    /**
     * Default home page model.
     * @return default home page model.
     */
    private ModelAndView getRegisterModel() {
        ModelAndView model = new ModelAndView("index");

        return model;
    }
}
