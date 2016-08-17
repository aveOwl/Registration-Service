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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfirmController.class)
public class ConfirmControllerTest {
    /**
     * URI to confirmation page.
     */
    private static final String RESOURCE_URI = "/registration/confirm";

    /**
     * A {@link MockMvc} instance.
     */
    @Autowired
    protected MockMvc mvc;

    /**
     * A mocked {@link UserService}.
     */
    @MockBean
    private UserService userService;

    /**
     * Should attempt user confirmation on
     * valid confirmation link.
     * @throws Exception on error.
     */
    @Test
    public void shouldConfirm() throws Exception {
        doNothing().when(userService).confirm("test-hash");

        this.mvc.perform(get(RESOURCE_URI + "/test-hash"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/success"));

        verify(userService, atLeastOnce()).confirm("test-hash");
    }

    /**
     * Should render success page.
     * @throws Exception on error.
     */
    @Test
    public void shouldDisplaySuccessPage() throws Exception {
        this.mvc.perform(get("/success").accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("success"));
    }
}
