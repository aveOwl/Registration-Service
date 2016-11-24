package com.registration.util;

import com.registration.model.User;

import javax.mail.internet.MimeMessage;

/**
 * The EmailBuilder interface defines how an email should be build
 * for specified {@link User}.
 */
public interface EmailBuilder {

    /**
     * Constructs email for specified user.
     *
     * @param user
     * @return constructed email.
     */
    MimeMessage createEmail(User user);
}
