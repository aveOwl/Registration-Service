package com.registration.service.impl;

import com.registration.model.User;
import com.registration.service.MailService;
import com.registration.util.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

    private EmailBuilder emailBuilder;

    @Autowired
    public MailServiceImpl(final EmailBuilder emailBuilder) {
        this.emailBuilder = emailBuilder;
    }

    @Override
    @Async
    public void sendEmail(final User user) {
        Assert.notNull(user);

        this.emailBuilder.setRecipient(user);
        this.emailBuilder.sendEmail();

        LOG.info("Message sent.");
    }
}
