package com.registration.service;

import com.registration.model.User;

/**
 * The MailService interface defines all public business behaviours
 * associated with email operations such as configuring,
 * composing and transmitting an email for {@link User} entities.
 *
 * @author Bohdan Bachkala
 */
public interface MailService {

    /**
     * Constructs and sends confirmation email.
     *
     * @param user user for whom email is build and sent.
     */
    void sendEmail(User user);
}
