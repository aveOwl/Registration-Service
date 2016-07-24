package com.registration.controller;

import com.registration.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.registration.util.Points.CONFIRM;
import static com.registration.util.Points.CONFIRM_VIEW;
import static com.registration.util.Points.MAIN;
import static com.registration.util.Points.MAIN_VIEW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@WebMvcTest
public class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    
    private static final String VALID_EMAIL = "sample@gmail.com";

    private static final String INVALID_EMAIL = "same@.qwe.com";

    private static final String VALID_PASSWORD = "password12!";

    private static final String INVALID_PASSWORD = "password";

    @Test
    public void shouldDisplayDefaultHomePage() throws Exception {
        mvc.perform(get(MAIN))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(MAIN_VIEW));
    }

    @Test
    public void shouldReturnCurrentPageOnInvalidInput() throws Exception {
        mvc.perform(post(MAIN)
                .param("email", VALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "Pattern"))
                .andExpect(view().name(MAIN_VIEW));

        mvc.perform(post(MAIN)
                .param("email", INVALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "Email"))
                .andExpect(view().name(MAIN_VIEW));

        mvc.perform(post(MAIN)
                .param("email", INVALID_EMAIL)
                .param("password", INVALID_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "Email"))
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "Pattern"))
                .andExpect(view().name(MAIN_VIEW));
    }

    @Test
    public void shouldRedirectToConfirmPageOnValidInput() throws Exception {
        mvc.perform(post(MAIN)
                .param("email", VALID_EMAIL)
                .param("password", VALID_PASSWORD))
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl(MAIN + CONFIRM));
    }

    @Test
    public void shouldDisplayConfirmMessage() throws Exception {
        mvc.perform(get(MAIN + CONFIRM))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name(CONFIRM_VIEW));
    }
}
