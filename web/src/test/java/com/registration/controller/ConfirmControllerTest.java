package com.registration.controller;

import com.registration.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfirmController.class)
public class ConfirmControllerTest {

    private static final String CONFIRMATION_URI = "/registration/confirm";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConfirmController confirmController;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.standaloneSetup(confirmController).build();
    }

    @Test
    public void shouldConfirmOnValidLink() throws Exception {
        final String hash = "test-hash";

        doNothing().when(userService).confirm(hash);

        this.mvc.perform(get(CONFIRMATION_URI + "/" + hash))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/success"));

        verify(userService, only()).confirm(hash);
    }
}
