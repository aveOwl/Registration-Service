package com.registration.util;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MimeMessageHelperProviderImpl implements MimeMessageHelperProvider {
    private static final String CHARACTER_ENCODING = "UTF-8";

    /**
     * {@inheritDoc}
     */
    @Override
    public MimeMessageHelper getMimeMessageHelper(final MimeMessage message) throws MessagingException {
        return new MimeMessageHelper(message, true, CHARACTER_ENCODING);
    }
}
