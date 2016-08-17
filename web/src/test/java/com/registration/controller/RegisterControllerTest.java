package com.registration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
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

import javax.servlet.http.HttpServletRequest;

import static com.registration.Points.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {
    /**
     * URI to registration page.
     */
    private static final String REGISTRATION_URI = "/registration";

    /**
     * A {@link MockMvc} instance.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * A mocked {@link UserService}.
     */
    @MockBean
    private UserService userService;

    /**
     * A mocked {@link MailService}.
     */
    @MockBean
    private MailService mailService;

    private String inputJson;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    /**
     * Should redirect from root path onto registration page.
     * @throws Exception on error.
     */
    @Test
    public void shouldRedirectToHomePage() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REGISTRATION_URI));
    }

    /**
     * Should display home page.
     * @throws Exception on error.
     */
    @Test
    public void shouldDisplayDefaultHomePage() throws Exception {
        this.mvc.perform(get(REGISTRATION_URI).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("index"));
    }

    /**
     * Should refuse to attempt user registration if
     * provided email already exists.
     * @throws Exception on error.
     */
    @Test
    public void shouldNotRegisterUserOnDuplicateEmail() throws Exception {
        given(userService.findByEmail(VALID_EMAIL))
                .willReturn(user);

        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        inputJson = mapToJson(user);

        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(false)))
                .andExpect(jsonPath("$.emailViolationMessage", is(DUPLICATE_EMAIL_MSG)))
                .andExpect(jsonPath("$.passwordViolationMessage", isEmptyString()));

        verify(userService, never()).create(user);
        verify(mailService, never()).sendMail(user);
    }

    /**
     * Should refuse to attempt user registration if
     * provided email is invalid according to {@link User} constraints.
     * @throws Exception on error.
     */
    @Test
    public void shouldNotRegisterUserOnInvalidEmail() throws Exception {
        user.setEmail(INVALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        inputJson = mapToJson(user);

        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(false)))
                .andExpect(jsonPath("$.emailViolationMessage", is(INVALID_EMAIL_MSG)))
                .andExpect(jsonPath("$.passwordViolationMessage", isEmptyString()));

        verify(userService, never()).create(user);
        verify(mailService, never()).sendMail(user);
    }

    /**
     * Should refuse to attempt user registration if
     * provided password is invalid according to {@link User} constraints.
     * @throws Exception on error.
     */
    @Test
    public void shouldNotRegisterUserOnInvalidPassword() throws Exception {
        user.setEmail(VALID_EMAIL);
        user.setPassword(INVALID_PASSWORD);

        inputJson = mapToJson(user);

        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(true)))
                .andExpect(jsonPath("$.emailViolationMessage", isEmptyString()))
                .andExpect(jsonPath("$.passwordViolationMessage", is(INVALID_PASSWORD_MSG)));

        verify(userService, never()).create(user);
        verify(mailService, never()).sendMail(user);
    }

    /**
     * Should refuse to attempt user registration if both
     * provided password and email is invalid according to {@link User} constraints.
     * @throws Exception on error.
     */
    @Test
    public void shouldNotRegisterUserOnInvalidPasswordAndInvalidEmail() throws Exception {
        user.setEmail(INVALID_EMAIL);
        user.setPassword(INVALID_PASSWORD);

        inputJson = mapToJson(user);

        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(true)))
                .andExpect(jsonPath("$.emailViolationMessage", is(INVALID_EMAIL_MSG)))
                .andExpect(jsonPath("$.passwordViolationMessage", is(INVALID_PASSWORD_MSG)));

        verify(userService, never()).create(user);
        verify(mailService, never()).sendMail(user);
    }

    /**
     * Should attempt user registration if both provided password
     * and email is valid according to {@link User} constraints.
     * @throws Exception on error.
     */
    @Test
    public void shouldRegisterUserOnValidInput() throws Exception {
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        inputJson = mapToJson(user);

        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(false)))
                .andExpect(jsonPath("$.emailViolationMessage", isEmptyString()))
                .andExpect(jsonPath("$.passwordViolationMessage", isEmptyString()));

        verify(userService, atLeastOnce()).create(user);
        verify(mailService, atLeastOnce()).sendMail(user);
    }

    /**
     * Maps an object into Json String. Uses a Jackson ObjectMapper.
     *
     * @param obj the object to map.
     * @return object if form of Json String.
     * @throws JsonProcessingException if error occur.
     */
    private String mapToJson(final Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper.writeValueAsString(obj);
    }
}