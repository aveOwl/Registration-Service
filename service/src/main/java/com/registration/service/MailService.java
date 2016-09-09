package com.registration.service;

import com.registration.model.User;
import com.registration.service.impl.MailServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     The MailService interface defines all public business behaviours
 *     associated with email operations such as configuring, composing and transmitting
 *     an email for {@link User} entities.
 * </p>
 * <p>
 *     This interface should be injected into MailService clients, not the
 *     {@link MailServiceImpl} class.
 * </p>
 */
public interface MailService {

    /**
     * Constructs and sends confirmation email.
     * @param user user for whom email is build and sent.
     */
    void sendMail(User user);
}
