package com.registration.util.impl

import com.registration.model.User
import freemarker.template.Configuration
import freemarker.template.Template
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import spock.lang.Specification
import spock.lang.Unroll

import static com.registration.Points.VALID_EMAIL
import static com.registration.Points.VALID_PASSWORD

@Unroll
class EmailConfigurerSpec extends Specification {
    def freeMarker = Mock(FreeMarkerConfigurer) // TODO mocking class is bad
    def configuration = Mock(Configuration)
    def template = Mock(Template)

    def emailConfigurer = [freeMarker] as EmailConfigurerImpl
    def user = [VALID_EMAIL, VALID_PASSWORD] as User

    def "should construct email body"() {
        when:
        emailConfigurer.getEmailBody(user)

        then:
        1 * freeMarker.createConfiguration() >> configuration
        1 * configuration.getTemplate(_) >> template
    }
}
