package com.registration.controller;

import com.registration.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    
    private static final String VIEW_NAME = "index";

    private static final String VALID_EMAIL = "sample@gmail.com";

    private static final String INVALID_EMAIL = "same@.qwe.com";

    private static final String VALID_PASSWORD = "password12!";

    private static final String INVALID_PASSWORD = "password";

    @Test
    public void shouldRedirectFromRootContext() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/registration"));
    }

    @Test
    public void shouldDisplayDefaultHomePage() throws Exception {
        mvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("home", true))
                .andExpect(view().name(VIEW_NAME));
    }

    @Test
    public void shouldDisplayFormPage() throws Exception {
        mvc.perform(get("/registration/form"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("register", true))
                .andExpect(view().name(VIEW_NAME));
    }

    @Test
    public void shouldReturnCurrentPageOnInvalidInput() throws Exception {
        mvc.perform(post("/registration/form")
                .param("email", VALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "Pattern"))
                .andExpect(model().attribute("register", true))
                .andExpect(view().name(VIEW_NAME));

        mvc.perform(post("/registration/form")
                .param("email", INVALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "Email"))
                .andExpect(model().attribute("register", true))
                .andExpect(view().name(VIEW_NAME));

        mvc.perform(post("/registration/form")
                .param("email", INVALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "Email"))
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "Pattern"))
                .andExpect(model().attribute("register", true))
                .andExpect(view().name(VIEW_NAME));
    }

    @Test
    public void shouldRedirectToConfirmPageOnValidInput() throws Exception {
        mvc.perform(post("/registration/form")
                .param("email", VALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/registration/form/confirm"));
    }
}
