package com.registration.controller;

import com.registration.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.only;
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
    private MockMvc mvc;

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
        final String hash = "test-hash";

        doNothing().when(userService).confirm(hash);

        this.mvc.perform(get(RESOURCE_URI + "/" + hash))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/success"));

        verify(userService, only()).confirm(hash);
    }
}
