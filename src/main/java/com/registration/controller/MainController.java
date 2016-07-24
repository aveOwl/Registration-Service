package com.registration.controller;

import com.registration.entity.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.registration.util.Points.CONFIRM;
import static com.registration.util.Points.CONFIRM_VIEW;
import static com.registration.util.Points.MAIN;
import static com.registration.util.Points.MAIN_VIEW;

/**
 * Manages all incoming requests.
 */
@RestController
@RequestMapping(MAIN)
public class MainController {

    /**
     * Logging system.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    /**
     * {@link UserService} provides communication with
     * {@link UserRepository}.
     */
    private final UserService userService;

    /**
     * Injects required dependencies.
     * @param userService {@link UserService}
     */
    @Autowired
    public MainController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * User to be verified and saved.
     * @return user entity.
     */
    @ModelAttribute
    public User getUser() {
        return new User();
    }

    /**
     * Home page and form for user to submit.
     * @param user user entity for verification.
     * @return main view.
     */
    @GetMapping
    public ModelAndView home(final @ModelAttribute User user) {
        LOG.info("Resolving home page...");
        return new ModelAndView(MAIN_VIEW);
    }

    /**
     * Verifies submitted form fields against user entity
     * constraints. Attempts saving user to database if verification
     * allows that and redirects user to confirm page, otherwise
     * returns form page with error info.
     * @param user user entity to be saved into the database.
     * @param bindingResult contains errors.
     * @return without errors redirects to confirm page, otherwise
     * returns form page with error info.
     */
    @PostMapping
    public ModelAndView register(final @ModelAttribute @Valid User user,
                                 final BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            LOG.debug("User: {} is verified.", user);
            LOG.debug("Saving user: {}", user);
            userService.save(user);
            LOG.info("Redirecting to confirm page...");
            return new ModelAndView("redirect:" + MAIN + CONFIRM);
        }

        LOG.error("User: {} verification failed.", user);
        LOG.info("Returning form page with validation error info.");
        return new ModelAndView(MAIN_VIEW);
    }

    /**
     * Returns confirm page view.
     * @return confirm page view
     */
    @GetMapping(value = CONFIRM)
    public ModelAndView confirm() {
        return new ModelAndView(CONFIRM_VIEW);
    }
}
