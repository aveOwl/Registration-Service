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
                .andExpect(view().name("index"));
    }

    @Test
    public void shouldObtainUserInfoAndSaveUser() throws Exception {
        mvc.perform(post("/registration/form")
                .param("email", "test")
                .param("password", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("confirm", true))
                .andExpect(view().name("index"));
    }

    @Test
    public void shouldDisplayFormPage() throws Exception {
        mvc.perform(get("/registration/form"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("register", true))
                .andExpect(view().name("index"));
    }
}
