package com.registration.service.impl;

import com.registration.model.User;
import com.registration.service.ConfirmService;
import com.registration.util.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

@Service
public class ConfirmServiceImpl implements ConfirmService {

    private EmailBuilder emailBuilder;

    @Autowired
    public void setEmailBuilder(EmailBuilder emailBuilder) {
        this.emailBuilder = emailBuilder;
    }

    @Override
    public void confirm(final User user, final HttpServletRequest request) {
        Assert.notNull(user);
        emailBuilder.sendEmail(user, request);
    }
}
