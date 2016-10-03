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

import java.util.Optional;

import static com.registration.Points.DUPLICATE_EMAIL_MSG;
import static com.registration.Points.TEST_ERROR_MSG;
import static com.registration.Points.INVALID_EMAIL;
import static com.registration.Points.INVALID_EMAIL_MSG;
import static com.registration.Points.INVALID_PASSWORD;
import static com.registration.Points.INVALID_PASSWORD_MSG;
import static com.registration.Points.VALID_EMAIL;
import static com.registration.Points.VALID_PASSWORD;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
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
    private static final String REGISTRATION_URI = "/registration";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MailService mailService;

    private String inputJson;

    private User user;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.standaloneSetup(
                new RegisterController(this.userService, this.mailService))
                .setControllerAdvice(new MainControllerAdvice())
                .build();

        this.user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        this.user = null;
    }

    @Test
    public void shouldRedirectToHomePage() throws Exception {
        // when
        this.mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REGISTRATION_URI));
    }

    @Test
    public void shouldRenderHomePage() throws Exception {
        // when
        this.mvc.perform(get(REGISTRATION_URI))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("index"));
    }

    @Test
    public void shouldNotRegisterUserOnDuplicateEmail() throws Exception {
        // given
        given(this.userService.findByEmail(VALID_EMAIL))
                .willReturn(Optional.of(this.user));

        this.user.setEmail(VALID_EMAIL);
        this.user.setPassword(VALID_PASSWORD);

        this.inputJson = mapToJson(this.user);

        // when
        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(false)))
                .andExpect(jsonPath("$.emailViolationMessage", is(DUPLICATE_EMAIL_MSG)))
                .andExpect(jsonPath("$.passwordViolationMessage", isEmptyString()));

        // then
        verify(this.userService, never()).create(this.user);
        verify(this.mailService, never()).sendEmail(this.user);
    }

    @Test
    public void shouldNotRegisterUserOnInvalidEmail() throws Exception {
        // given
        this.user.setEmail(INVALID_EMAIL);
        this.user.setPassword(VALID_PASSWORD);

        this.inputJson = mapToJson(this.user);

        // when
        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(false)))
                .andExpect(jsonPath("$.emailViolationMessage", is(INVALID_EMAIL_MSG)))
                .andExpect(jsonPath("$.passwordViolationMessage", isEmptyString()));

        // then
        verify(this.userService, never()).create(this.user);
        verify(this.mailService, never()).sendEmail(this.user);
    }

    @Test
    public void shouldNotRegisterUserOnInvalidPassword() throws Exception {
        // given
        this.user.setEmail(VALID_EMAIL);
        this.user.setPassword(INVALID_PASSWORD);

        this.inputJson = mapToJson(this.user);

        // when
        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(true)))
                .andExpect(jsonPath("$.emailViolationMessage", isEmptyString()))
                .andExpect(jsonPath("$.passwordViolationMessage", is(INVALID_PASSWORD_MSG)));

        // then
        verify(this.userService, never()).create(this.user);
        verify(this.mailService, never()).sendEmail(this.user);
    }

    @Test
    public void shouldNotRegisterUserOnInvalidPasswordAndInvalidEmail() throws Exception {
        // given
        this.user.setEmail(INVALID_EMAIL);
        this.user.setPassword(INVALID_PASSWORD);

        this.inputJson = mapToJson(this.user);

        // when
        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(true)))
                .andExpect(jsonPath("$.emailViolationMessage", is(INVALID_EMAIL_MSG)))
                .andExpect(jsonPath("$.passwordViolationMessage", is(INVALID_PASSWORD_MSG)));

        // then
        verify(this.userService, never()).create(this.user);
        verify(this.mailService, never()).sendEmail(this.user);
    }

    @Test
    public void shouldRegisterUserOnValidEmailAndPassword() throws Exception {
        // given
        this.user.setEmail(VALID_EMAIL);
        this.user.setPassword(VALID_PASSWORD);

        given(this.userService.findByEmail(VALID_EMAIL))
                .willReturn(Optional.empty());

        this.inputJson = mapToJson(this.user);

        // when
        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(false)))
                .andExpect(jsonPath("$.emailViolationMessage", isEmptyString()))
                .andExpect(jsonPath("$.passwordViolationMessage", isEmptyString()));

        // then
        verify(this.userService, atLeastOnce()).create(this.user);
        verify(this.mailService, atLeastOnce()).sendEmail(this.user);
    }

    @Test
    public void shouldRenderErrorPageWithInternalServerErrorStatus() throws Exception {
        // given
        this.user.setEmail(VALID_EMAIL);
        this.user.setPassword(VALID_PASSWORD);

        given(this.userService.findByEmail(this.user.getEmail()))
                .willReturn(Optional.empty());

        this.inputJson = mapToJson(this.user);

        doThrow(new IllegalArgumentException(TEST_ERROR_MSG))
                .when(this.userService).create(this.user);

        // when
        this.mvc.perform(post(REGISTRATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.inputJson))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("description", containsString(TEST_ERROR_MSG)));

        // then
        verify(this.userService, atLeastOnce()).create(this.user);
        verify(this.mailService, never()).sendEmail(this.user);
    }

    /**
     * Maps an object into Json String. Uses a Jackson ObjectMapper.
     *
     * @param obj the object to map.
     * @return object in form of Json String.
     * @throws JsonProcessingException on error.
     */
    private String mapToJson(final Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper.writeValueAsString(obj);
    }
}