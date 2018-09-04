package com.registration.service.impl;

import com.registration.model.User;
import com.registration.service.MailService;
import com.registration.util.EmailBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final EmailBuilder emailBuilder;
    private final JavaMailSender mailSender;

    /**
     * {@inheritDoc}
     */
    @Override
    @Async
    public void sendEmail(User user) {
        Assert.notNull(user, "User can't be null.");
        val email = this.emailBuilder.createEmail(user);
        this.mailSender.send(email);
        log.info("Email successfully sent.");
    }
}
