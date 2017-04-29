package com.registration.util;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

/**
 * The MimeMessageHelperProvider interface defines how an instance
 * of {@link MimeMessageHelper} should be built.
 *
 * @author Bohdan Bachkala
 */
public interface MimeMessageHelperProvider {

    /**
     * Constructs an instance of {@link MimeMessageHelper} in multipart mode
     * with the specific character encoding.
     *
     * @return an instance of {@link MimeMessageHelper} in multipart mode with the specific character encoding.
     * @throws MessagingException on error.
     */
    MimeMessageHelper getMimeMessageHelper() throws MessagingException;
}
