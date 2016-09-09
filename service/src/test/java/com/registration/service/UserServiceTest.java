package com.registration.service;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import com.registration.service.impl.UserServiceImpl;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import static com.registration.Points.VALID_EMAIL;
import static com.registration.Points.VALID_PASSWORD;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.atLeastOnce;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.only;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServiceImpl.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

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
    public void shouldFindUserByEmail() throws Exception {
        given(userRepository.findByEmail(VALID_EMAIL))
                .willReturn(user);

        final User user = userService.findByEmail(VALID_EMAIL);

        assertThat("should contain user password",
                user.getPassword(), is(VALID_PASSWORD));
        assertThat("should contain user email",
                user.getEmail(), is(VALID_EMAIL));
    }

    @Test
    public void shouldCreateUser() throws Exception {
        user.setId(null);
        
        userService.create(user);

        verify(userRepository, only()).save(userCaptor.capture());

        assertThat("should contain corresponding user email",
                user.getEmail(), is(userCaptor.getValue().getEmail()));
        assertThat("should contain corresponding user password",
                user.getPassword(), is(userCaptor.getValue().getPassword()));
    }

    @Test(expected = EntityExistsException.class)
    public void shouldFailToCreateUser() throws Exception {
        user.setId(99L);

        userService.create(user);

        verify(userRepository, never()).save(user);
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        user.setId(99L);

        userService.update(user);

        verify(userRepository, only()).save(userCaptor.capture());

        assertThat("should contain corresponding user email",
                user.getEmail(), is(userCaptor.getValue().getEmail()));
        assertThat("should contain corresponding user password",
                user.getPassword(), is(userCaptor.getValue().getPassword()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldFailToUpdateWithNullId() throws Exception {
        user.setId(null);

        userService.update(user);

        verify(userRepository, never()).save(user);
    }


    @Test
    public void shouldConfirmUser() throws Exception {
        final String code = Base64Utils.encodeToString(VALID_EMAIL.getBytes());

        given(userRepository.findByEmail(VALID_EMAIL))
                .willReturn(user);

        user.setId(99L);

        userService.confirm(code);

        verify(userRepository, atLeastOnce()).save(userCaptor.capture());

        assertThat("user should be confirmed",
                userCaptor.getValue().isConfirmed(), is(true));
        assertThat("user should have valid email",
                userCaptor.getValue().getEmail(), is(VALID_EMAIL));
        assertThat("user should have valid password",
                userCaptor.getValue().getPassword(), is(VALID_PASSWORD));
    }

    @Test(expected = NoResultException.class)
    public void shouldFailToConfirmUser() throws Exception {
        given(userRepository.findByEmail(VALID_EMAIL))
                .willReturn(null);

        userService.confirm("invalid code");

        verify(userRepository, never()).save(user);

        assertThat("user should not be confirmed",
                user.isConfirmed(), is(false));
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
