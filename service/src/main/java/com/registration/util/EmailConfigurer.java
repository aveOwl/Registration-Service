package com.registration.util;

import com.registration.model.User;
import org.springframework.core.io.Resource;

/**
 * The EmailConfigurer interface defines how an email body
 * should be constructed.
 *
 * @author Bohdan Bachkala
 */
public interface EmailConfigurer {

    /**
     * Constructs String representation of an email body
     * for specified user.
     *
     * @param user
     * @return string representation of an email body.
     */
    String getEmailBody(User user);

    /**
     * Returns resources for email construction.
     *
     * @return resources for email construction.
     */
    Resource getEmailResource();
}
