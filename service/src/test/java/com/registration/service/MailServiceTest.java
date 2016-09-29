package com.registration.service;

import com.registration.model.User;
import com.registration.service.impl.MailServiceImpl;
import com.registration.util.EmailBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static com.registration.Points.VALID_EMAIL;
import static com.registration.Points.VALID_PASSWORD;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailServiceImpl.class)
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @MockBean
    private EmailBuilder emailBuilder;

    @MockBean
    private MimeMessage email;

    @MockBean
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldCreateAndSendEmail() throws Exception {
        // given
        given(emailBuilder.createEmail(user))
                .willReturn(email);
        doNothing().when(mailSender).send(email);

        // when
        mailService.sendEmail(user);

        // then
        verify(emailBuilder, atLeastOnce()).createEmail(userCaptor.capture());
        verify(mailSender, atLeastOnce()).send(email);

        assertThat("user email",
                userCaptor.getValue().getEmail(), is(VALID_EMAIL));
        assertThat("user password",
                userCaptor.getValue().getPassword(), is(VALID_PASSWORD));
    }

    @Test
    public void shouldThrowExceptionOnNullUser() throws Exception {
        thrown.expect(IllegalArgumentException.class);

        // when
        mailService.sendEmail(null);

        // then
        verify(emailBuilder, never()).createEmail(user);
        verify(mailSender, never()).send(email);
    }
}