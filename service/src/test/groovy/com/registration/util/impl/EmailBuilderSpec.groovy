package com.registration.util.impl

import com.registration.model.User
import com.registration.util.EmailConfigurer
import com.registration.util.MimeMessageHelperProvider
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.MimeMessageHelper
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class EmailBuilderSpec extends Specification {
    def configurer = Mock(EmailConfigurer)
    def provider = Mock(MimeMessageHelperProvider)
    def logo = Mock(Resource)
    def helper = Mock(MimeMessageHelper) // TODO mocking classes is bad

    def builder = [configurer, provider] as EmailBuilderImpl

    def user = ["test@gmail.com", "testpass111"] as User
    def testBody = "test-body"

    def "should create email"() {
        when:
        builder.createEmail(user)

        then:
        1 * provider.getMimeMessageHelper() >> helper
        1 * configurer.getEmailResource() >> logo
        1 * configurer.getEmailBody(user) >> testBody

        with(helper) {
            1 * setFrom(_)
            1 * setSubject(_)
            1 * setTo(user.getEmail())
            1 * setText(testBody, true)
            1 * addInline(_, logo)
            0 * _
        }
    }
}
