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

import javax.persistence.EntityExistsException;
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
    private ConfirmService confirmService;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private UserRepository userRepository;


    private static final String EMAIL = "email@domain.com";
    private static final String PASSWORD = "password";

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User(EMAIL, PASSWORD);
        doNothing().when(confirmService).confirm(user, request);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldReturnUserByEmail() throws Exception {
        given(userRepository.findByEmail(EMAIL))
                .willReturn(user);

        User byEmail = userService.findByEmail(EMAIL);

        assertEquals(byEmail.getPassword(), PASSWORD);
    }

    @Test
    public void shouldCreateUser() throws Exception {
        given(userRepository.findByEmail(EMAIL))
                .willReturn(null);

        userService.create(user);

        verify(userRepository, atLeastOnce()).saveAndFlush(user);
    }

    @Test(expected = EntityExistsException.class)
    public void shouldThrowExceptionOnDuplicateEmail() throws Exception {
        given(userRepository.findByEmail(EMAIL))
                .willReturn(user);

        userService.create(user);

        verify(userRepository, never()).saveAndFlush(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullUser() throws Exception {
        userService.create(null);
    }
}