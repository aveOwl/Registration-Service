package com.registration.controller;

import com.registration.entity.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
     * {@link UserService} provides communication with
     * {@link UserRepository}.
     */
    private final UserService userService;

    /**
     * Injects required dependencies.
     * @param userService {@link UserService}
     */
    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User getUserModel() {
        return new User();
    }

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
        LOG.info("displaying home page...");
        return getDefaultModel().addObject("home", true);
    }

    /**
     * Displays form page.
     * @return form page model.
     */
    @RequestMapping(value = "/registration/form", method = RequestMethod.GET)
    public ModelAndView showFormPage(@ModelAttribute("user") User user) {
        LOG.info("displaying form page...");
        return getDefaultModel().addObject("register", true);
    }

    /**
     * With obtained parameters creates user entity and provides it
     * to @{@link UserService} in order to persist user entity into
     * the database.
     * @return model of conformation page.
     */
    @RequestMapping(value = "/registration/form", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            LOG.error("invalid email: {}", user.getEmail());
            LOG.error("invalid password: {}", user.getPassword());
            return getDefaultModel().addObject("register", true);
        }
        userService.save(user);
        LOG.info("redirecting to confirm page...");

        return new ModelAndView("redirect:/registration/form/confirm");
    }

    @RequestMapping(value = "/registration/form/confirm", method = RequestMethod.GET)
    public ModelAndView getConfirmPage() {
        return getDefaultModel().addObject("confirm", true);
    }

    /**
     * Default home page model.
     * @return default home page model.
     */
    private ModelAndView getDefaultModel() {
        ModelAndView model = new ModelAndView("index");

        return model;
    }
}
