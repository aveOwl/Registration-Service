package com.registration.util.impl;

import com.registration.util.MimeMessageHelperProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MimeMessageHelperProviderImpl implements MimeMessageHelperProvider {
    private static final String CHARACTER_ENCODING = "UTF-8";

    private JavaMailSender mailSender;

    @Autowired
    public MimeMessageHelperProviderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MimeMessageHelper getMimeMessageHelper() throws MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();

        return new MimeMessageHelper(message, true, CHARACTER_ENCODING);
    }
}
