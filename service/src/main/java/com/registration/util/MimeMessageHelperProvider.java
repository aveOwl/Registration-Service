package com.registration.util;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * <p>
 *     The MimeMessageHelperProvider interface defines how an instance
 *     of {@link MimeMessageHelper} should be built.
 *     Constructs an instance of {@link MimeMessageHelper} in multipart mode
 *     with the specific character encoding.
 * </p>
 * <p>
 *     This interface should be injected into MimeMessageHelperProvider clients, not the
 *     {@link MimeMessageHelperProviderImpl} class.
 * </p>
 */
public interface MimeMessageHelperProvider {
    MimeMessageHelper getMimeMessageHelper(MimeMessage message) throws MessagingException;
}
