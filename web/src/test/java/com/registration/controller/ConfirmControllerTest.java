package com.registration.controller;

import com.registration.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.NoResultException;

import static com.registration.Points.TEST_ERROR_MSG;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
    private static final String CONFIRMATION_URI = "/registration/confirm";
    private static final String TEST_HASH = "test-hash";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.standaloneSetup(new ConfirmController(this.userService))
                .setControllerAdvice(new MainControllerAdvice())
                .build();
    }

    @Test
    public void shouldConfirmOnValidLink() throws Exception {
        // given
        doNothing().when(this.userService).confirm(TEST_HASH);

        // when
        this.mvc.perform(get(CONFIRMATION_URI + "/" + TEST_HASH))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/success"));

        // then
        verify(this.userService, only()).confirm(TEST_HASH);
    }

    @Test
    public void shouldRenderSuccessPage() throws Exception {
        // when
        this.mvc.perform(get("/success"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("success-page"));
    }

    @Test
    public void shouldRenderErrorPageWithBadRequestStatus() throws Exception {
        // given
        doThrow(new NoResultException(TEST_ERROR_MSG))
                .when(this.userService).confirm(TEST_HASH);

        // when
        this.mvc.perform(get(CONFIRMATION_URI + "/" + TEST_HASH))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("description", containsString(TEST_ERROR_MSG)));

        // then
        verify(this.userService, only()).confirm(TEST_HASH);
    }
}
