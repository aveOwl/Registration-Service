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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RegisterControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    private User user;

    private String inputJson;

    private static final String VALID_EMAIL = "redowlave@gmail.com";

    private static final String INVALID_EMAIL = "same@.qwe.com";

    private static final String VALID_PASSWORD = "password12!";

    private static final String INVALID_PASSWORD = "password";

    private static final String REGISTRATION = "/registration";

    @Before
    public void setUp() throws Exception {
        user = new User();
        inputJson = mapToJson(user);
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
    public void verifyInvalidEmailValidPassword() throws Exception {
        user.setEmail(INVALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        inputJson = mapToJson(user);

        mvc.perform(post(REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(false)));
    }

    @Test
    public void verifyValidEmailInvalidPassword() throws Exception {
        user.setEmail(VALID_EMAIL);
        user.setPassword(INVALID_PASSWORD);

        inputJson = mapToJson(user);

        mvc.perform(post(REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(true)));
    }

    @Test
    public void verifyInvalidEmailInvalidPassword() throws Exception {
        user.setEmail(INVALID_EMAIL);
        user.setPassword(INVALID_PASSWORD);

        inputJson = mapToJson(user);

        mvc.perform(post(REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(true)));
    }

    @Test
    public void verifyValidEmailValidPassword() throws Exception {
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        inputJson = mapToJson(user);

        mvc.perform(post(REGISTRATION)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(false)));
    }

    @Test
    public void shouldRedirectToSuccessPage() throws Exception {
        mvc.perform(get("/success"))
                .andExpect(view().name("success"));
    }

    private String mapToJson(final Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper.writeValueAsString(obj);
    }
}