package com.registration.util;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * The MimeMessageHelperProvider interface defines how an instance
 * of {@link MimeMessageHelper} should be built.
 */
public interface MimeMessageHelperProvider {

    /**
     * Constructs an instance of {@link MimeMessageHelper} in multipart mode
     * with the specific character encoding.
     *
     * @param message a message to be constructed.
     * @return an instance of {@link MimeMessageHelper} in multipart mode with the specific character encoding.
     * @throws MessagingException on error.
     */
    MimeMessageHelper getMimeMessageHelper(MimeMessage message) throws MessagingException;
}
