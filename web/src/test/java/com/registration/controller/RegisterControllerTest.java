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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

    private static final String REGISTRATION_URI = "/registration";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RegisterController registerController;

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    private static String inputJson;

    private static User user;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.standaloneSetup(registerController).build();

        user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldRedirectToHomePage() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REGISTRATION_URI));
    }

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
        verify(mailService, never()).sendEmail(user);
    }

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
        verify(mailService, never()).sendEmail(user);
    }

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
        verify(mailService, never()).sendEmail(user);
    }

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
        verify(mailService, never()).sendEmail(user);
    }

    @Test
    public void shouldRegisterUserOnValidEmailAndPassword() throws Exception {
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
        verify(mailService, atLeastOnce()).sendEmail(user);
    }

    /**
     * Maps an object into Json String. Uses a Jackson ObjectMapper.
     * @param obj the object to map.
     * @return object in form of Json String.
     * @throws JsonProcessingException on error.
     */
    private String mapToJson(final Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper.writeValueAsString(obj);
    }
}