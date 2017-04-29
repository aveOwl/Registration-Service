package com.registration.util.impl;

import com.registration.util.MimeMessageHelperProvider;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MimeMessageHelperProviderImpl implements MimeMessageHelperProvider {
    private final JavaMailSender mailSender;

    /**
     * {@inheritDoc}
     */
    @Override
    public MimeMessageHelper getMimeMessageHelper() throws MessagingException {
        val message = this.mailSender.createMimeMessage();

        return new MimeMessageHelper(message, true, "UTF-8");
    }
}
