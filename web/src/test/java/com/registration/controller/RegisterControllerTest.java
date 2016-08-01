package com.registration.controller;

import com.registration.service.ConfirmService;
import com.registration.service.UserService;
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
    private ConfirmService confirmService;

    private static final String VALID_EMAIL = "redowlave@gmail.com";

    private static final String INVALID_EMAIL = "same@.qwe.com";

    private static final String VALID_PASSWORD = "password12!";

    private static final String INVALID_PASSWORD = "password";

    private static final String MAIN = "/registration";

    @Test
    public void shouldRedirectToHomePage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(MAIN));
    }

    @Test
    public void shouldDisplayDefaultHomePage() throws Exception {
        mvc.perform(get(MAIN).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("index"));
    }

    @Test
    public void verifyInvalidInput() throws Exception {
        mvc.perform(post(MAIN)
                .param("email", VALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(true)));

        mvc.perform(post(MAIN)
                .param("email", INVALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(false)));

        mvc.perform(post(MAIN)
                .param("email", INVALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.invalidEmail", is(true)))
                .andExpect(jsonPath("$.invalidPassword", is(true)));
    }

    @Test
    public void verifyValidInput() throws Exception {
        mvc.perform(post(MAIN)
                .param("email", VALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.invalidEmail", is(false)))
                .andExpect(jsonPath("$.invalidPassword", is(false)));
    }


}