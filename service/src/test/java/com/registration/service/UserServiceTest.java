package com.registration.service;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import com.registration.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.atLeastOnce;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServiceImpl.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private MailService mailService;

    @MockBean
    private UserRepository userRepository;

    private static final String EMAIL = "email@domain.com";
    private static final String PASSWORD = "password";

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User(EMAIL, PASSWORD);
        doNothing().when(mailService).sendMail(user);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldFindUserByEmail() throws Exception {
        given(userRepository.findByEmail(EMAIL))
                .willReturn(user);

        User user = userService.findByEmail(EMAIL);

        assertEquals(user.getPassword(), PASSWORD);
    }

    @Test
    public void shouldCreateUser() throws Exception {
        given(userRepository.findByEmail(EMAIL))
                .willReturn(null);

        userService.create(user);

        verify(userRepository, atLeastOnce()).saveAndFlush(user);
    }

    @Test(expected = EntityExistsException.class)
    public void shouldFailToCreateWithExistingEmail() throws Exception {
        given(userRepository.findByEmail(EMAIL))
                .willReturn(user);

        userService.create(user);

        verify(userRepository, never()).saveAndFlush(user);
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        user.setId(99L);

        userService.update(user);

        verify(userRepository, atLeastOnce()).saveAndFlush(user);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldFailToUpdateWithNullId() throws Exception {
        user.setId(null);

        userService.update(user);

        verify(userRepository, never()).saveAndFlush(user);
    }


    @Test
    public void shouldConfirmUser() throws Exception {
        final String code = Base64Utils.encodeToString(EMAIL.getBytes());

        given(userRepository.findByEmail(EMAIL))
                .willReturn(user);

        user.setId(99L);

        userService.confirm(code);

        verify(userRepository, atLeastOnce()).saveAndFlush(user);
    }

    @Test(expected = NoResultException.class)
    public void shouldFailToConfirmInvalidLink() throws Exception {
        given(userRepository.findByEmail(EMAIL))
                .willReturn(null);

        userService.confirm("invalid code");

        verify(userRepository, never()).saveAndFlush(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateNullUser() throws Exception {
        userService.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToUpdateNullUser() throws Exception {
        userService.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToFindByNullEmail() throws Exception {
        userService.findByEmail(null);
    }
}
