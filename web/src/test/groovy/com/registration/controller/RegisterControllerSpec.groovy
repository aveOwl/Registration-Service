package com.registration.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.registration.model.User
import com.registration.service.MailService
import com.registration.service.UserService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

import static com.registration.Points.*
import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.isEmptyString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(RegisterController)
@Unroll
class RegisterControllerSpec extends Specification {
    def userService = Mock(UserService)
    def mailService = Mock(MailService)

    def controller = [userService, mailService] as RegisterController
    def advice = [] as GlobalControllerAdvice
    def user = [] as User

    def mvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(advice).build()

    def "should redirect to home page"() {
        expect:
        mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/registration"))
    }

    def "should render home page"() {
        expect:
        mvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("index"))
    }

    def "should not register user on invalid input"() {
        given:
        userService.findByEmail(VALID_EMAIL) >> user

        user.email = email
        user.password = password
        def json = mapToJson(user)

        when:
        def response = mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))

        then:
        0 * userService.create(_)
        0 * mailService.sendEmail(_)

        and:
        response
                .andExpect(jsonPath('$.success', is(false)))
                .andExpect(jsonPath('$.invalidEmail', is(invalidEmail)))
                .andExpect(jsonPath('$.invalidPassword', is(invalidPass)))
                .andExpect(jsonPath('$.emailViolationMessage', is(emailViolation)))
                .andExpect(jsonPath('$.passwordViolationMessage', is(passViolation)))

        where:
        email         | password         | invalidEmail | invalidPass | emailViolation      | passViolation
        INVALID_EMAIL | VALID_PASSWORD   | true         | false       | INVALID_EMAIL_MSG   | isEmptyString()
        VALID_EMAIL   | INVALID_PASSWORD | false        | true        | isEmptyString()     | INVALID_PASSWORD_MSG
        INVALID_EMAIL | INVALID_PASSWORD | true         | true        | INVALID_EMAIL_MSG   | INVALID_PASSWORD_MSG
        VALID_EMAIL   | VALID_PASSWORD   | true         | false       | DUPLICATE_EMAIL_MSG | isEmptyString()
    }

    def "should register user on valid input"() {
        given:
        user.email = email
        user.password = password
        def json = mapToJson(user)

        when:
        def response = mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))

        then:
        1 * userService.create(user)
        1 * mailService.sendEmail(user)

        and:
        response
                .andExpect(jsonPath('$.success', is(true)))
                .andExpect(jsonPath('$.invalidEmail', is(invalidEmail)))
                .andExpect(jsonPath('$.invalidPassword', is(invalidPass)))
                .andExpect(jsonPath('$.emailViolationMessage', is(emailViolation)))
                .andExpect(jsonPath('$.passwordViolationMessage', is(passViolation)))

        where:
        email       | password       | invalidEmail | invalidPass | emailViolation  | passViolation
        VALID_EMAIL | VALID_PASSWORD | false        | false       | isEmptyString() | isEmptyString()
    }

    def "should render error page on null user"() {
        given:
        user.email = VALID_EMAIL
        user.password = VALID_PASSWORD
        def json = mapToJson(user)

        1 * userService.create(user) >> { throw new IllegalArgumentException(TEST_ERROR_MSG) }
        0 * userService.confirm(user)

        expect:
        mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("description", containsString(TEST_ERROR_MSG)))
    }

    def mapToJson(Object obj) {
        def mapper = [] as ObjectMapper
        mapper.registerModule([] as JodaModule)
        return mapper.writeValueAsString(obj)
    }
}
