package com.registration.controller;

import com.registration.entity.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import com.registration.util.FormValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.registration.util.Points.MAIN;
import static com.registration.util.Points.VIEW;

/**
 * The RegisterController class is a RESTful web service controller.
 * The <code>@RestController</code> annotation informs Spring that each
 * <code>@RequestMapping</code> method returns a <code>@ResponseBody</code>.
 */
@RestController
@RequestMapping(MAIN)
public class RegisterController {

    /**
     * Logging system.
     */
    private static final Logger LOG =
            LoggerFactory.getLogger(RegisterController.class);

    /**
     * {@link UserService} provides {@link User}
     * related services.
     */
    private final UserService userService;

    /**
     * Autowire {@link UserService} implementation.
     * @param userService {@link UserService}
     */
    @Autowired
    public RegisterController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * User to be verified and saved.
     * @return user entity.
     */
    @ModelAttribute("user")
    public User getUser() {
        return new User();
    }

    /**
     * Welcome / Registration page.
     * @return main view.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home() {
        LOG.info("Resolving home page...");
        return new ModelAndView(VIEW);
    }

    /**
     * Verifies submitted form fields against user entity
     * constraints. Attempts saving user to database if verification
     * allows that, otherwise displays form page with error info.
     * @param user user entity to be saved into the database.
     * @param bindingResult contains errors.
     * @return result of validation.
     */
    @RequestMapping(method = RequestMethod.POST)
    public FormValidationResult register(final @Valid User user,
                                         final BindingResult bindingResult) {
        LOG.info("Attempting user registration...");

        if (!bindingResult.hasErrors()) {
            LOG.debug("User: {} is verified.", user);
            userService.save(user);
        } else {
            LOG.error("User: {} verification failed.", user);
        }

        return new FormValidationResult(bindingResult);
    }
}
