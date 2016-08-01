package com.registration.controller;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

        final User userToConfirm = userService.findByEmail(data[0]);

        if (userToConfirm != null) {
            userToConfirm.setConfirmed(true);
            userService.update(userToConfirm);
            LOG.info("User: {} is confirmed", userToConfirm);
        }
        return "redirect:/success";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String confirm() {
        return "success";
    }
}
