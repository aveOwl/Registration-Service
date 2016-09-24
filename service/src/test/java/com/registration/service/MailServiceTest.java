package com.registration.service;

import com.registration.model.User;
import com.registration.service.impl.MailServiceImpl;
import com.registration.util.EmailBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.registration.Points.VALID_EMAIL;
import static com.registration.Points.VALID_PASSWORD;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MailServiceImpl.class)
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @MockBean
    private EmailBuilder emailBuilder;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private static User user;

    @Before
    public void setUp() throws Exception {
        user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldSendWithoutError() throws Exception {
        doNothing().when(emailBuilder).setRecipient(user);

        mailService.sendEmail(user);

        verify(emailBuilder, atLeastOnce()).setRecipient(userCaptor.capture());
        verify(emailBuilder, atLeastOnce()).sendEmail();

        assertThat("user email",
                userCaptor.getValue().getEmail(), is(VALID_EMAIL));
        assertThat("user password",
                userCaptor.getValue().getPassword(), is(VALID_PASSWORD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullUser() throws Exception {
        mailService.sendEmail(null);

        verify(emailBuilder, never()).sendEmail();
    }
}