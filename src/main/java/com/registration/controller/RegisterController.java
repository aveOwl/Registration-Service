package com.registration.controller;

import com.registration.entity.User;
import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
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
        ModelAndView model = getDefaultModel();

        model.addObject("home", true);

        return model;
    }

    /**
     * Displays form page.
     * @return form page model.
     */
    @RequestMapping(value = "/registration/form", method = RequestMethod.GET)
    public ModelAndView showFormPage() {
        LOG.info("displaying form page...");
        ModelAndView model = getDefaultModel();

        model.addObject("register", true);

        return model;
    }

    /**
     * With obtained parameters creates user entity and provides it
     * to @{@link UserService} in order to persist user entity into
     * the database.
     * @param email email provided by user.
     * @param password password provided by user.
     * @return model of conformation page.
     */
    @RequestMapping(value = "/registration/form", method = RequestMethod.POST)
    public ModelAndView register(@RequestParam("email") String email,
                                 @RequestParam("password") String password) {

        ModelAndView model = getDefaultModel();

        User user = new User(email, password);

        LOG.debug("saving new user: {}", user);
        userService.save(user);

        model.addObject("confirm", true);

        return model;
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
