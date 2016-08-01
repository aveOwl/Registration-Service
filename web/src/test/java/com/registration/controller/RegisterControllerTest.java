package com.registration.controller;

import com.registration.model.User;
import com.registration.service.MailService;
import com.registration.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RegisterControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    @MockBean
    private HttpServletRequest request;

    private User user;

    private static final String VALID_EMAIL = "redowlave@gmail.com";

    private static final String INVALID_EMAIL = "same@.qwe.com";

    private static final String VALID_PASSWORD = "password12!";

    private static final String INVALID_PASSWORD = "password";

    private static final String REGISTRATION = "/registration";

    private static final String CONFIRM = REGISTRATION + "/confirm/";

    @Before
    public void setUp() throws Exception {
        user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldRedirectToHomePage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REGISTRATION));
    }

    @Test
    public void shouldDisplayDefaultHomePage() throws Exception {
        mvc.perform(get(REGISTRATION).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("index"));
    }

    @Test
    public void verifyInvalidInput() throws Exception {
        mvc.perform(post(REGISTRATION)
                .param("email", VALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(true)));

        mvc.perform(post(REGISTRATION)
                .param("email", INVALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(false)));

        mvc.perform(post(REGISTRATION)
                .param("email", INVALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(true)));
    }

    @Test
    public void verifyValidInput() throws Exception {
        mvc.perform(post(REGISTRATION)
                .param("email", VALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(false)));
    }

    @Test
    public void shouldProcessConfirmation() throws Exception {
        String encodedData =
                Base64Utils.encodeToString((user.getEmail() + ":" + user.getPassword()).getBytes());

        doNothing().when(userService).confirm(VALID_EMAIL);

        mvc.perform(get(CONFIRM + encodedData))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/success"));
    }

    @Test
    public void shouldRedirectToSuccessPage() throws Exception {
        mvc.perform(get("/success"))
                .andExpect(view().name("success"));
    }
}