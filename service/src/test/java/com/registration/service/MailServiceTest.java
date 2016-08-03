package com.registration.service;

import com.registration.model.User;
import com.registration.service.impl.MailServiceImpl;
import com.registration.util.EmailBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MailServiceImpl.class)
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @MockBean
    private EmailBuilder emailBuilder;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldSendWithoutError() throws Exception {
        mailService.sendMail(user);

        verify(emailBuilder, atLeastOnce()).sendEmail(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullUser() throws Exception {
        mailService.sendMail(null);

        verify(emailBuilder, never()).sendEmail(null);
    }
}