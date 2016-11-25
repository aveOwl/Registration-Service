package com.registration.controller

import com.registration.service.UserService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.NoResultException

import static com.registration.Points.TEST_ERROR_MSG
import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ConfirmController)
@Unroll
class ConfirmControllerSpec extends Specification {
    def confirmation_uri = "/registration/confirm"
    def test_hash = "test-hash"

    def userService = Mock(UserService)

    def controller = [userService] as ConfirmController
    def advice = [] as GlobalControllerAdvice

    def mvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(advice).build()

    def "should confirm on valid confirmation link"() {
        when:
        mvc.perform(get(confirmation_uri + "/" + test_hash))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/success"))

        then:
        1 * userService.confirm(test_hash)
        0 * userService._
    }

    def "should render success page"() {
        expect:
        mvc.perform(get("/success"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("success-page"))
    }

    def "should render error page on invalid link"() {
        given:
        1 * userService.confirm(test_hash) >> { throw new NoResultException(TEST_ERROR_MSG) }

        expect:
        mvc.perform(get(confirmation_uri + "/" + test_hash))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("description", containsString(TEST_ERROR_MSG)))
    }
}
