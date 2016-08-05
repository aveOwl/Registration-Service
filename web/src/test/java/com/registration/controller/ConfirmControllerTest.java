package com.registration.controller;

import com.registration.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfirmController.class)
public class ConfirmControllerTest {

    @Autowired
    protected MockMvc mvc;

    /**
     * A mocked {@link UserService}.
     */
    @MockBean
    private UserService userService;

    private static final String RESOURCE_URI = "/registration/confirm";

    @Test
    public void shouldConfirm() throws Exception {
        doNothing().when(userService).confirm("test-hash");

        this.mvc.perform(get(RESOURCE_URI + "/test-hash"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/success"));

        verify(userService, atLeastOnce()).confirm("test-hash");
    }

    @Test
    public void shouldRedirect() throws Exception {
        this.mvc.perform(get("/success")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }
}