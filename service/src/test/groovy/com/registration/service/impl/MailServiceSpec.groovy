package com.registration.service.impl

import com.registration.model.User
import com.registration.util.EmailBuilder
import org.springframework.mail.javamail.JavaMailSender
import spock.lang.Specification
import spock.lang.Unroll

import javax.mail.internet.MimeMessage

import static com.registration.Points.VALID_EMAIL
import static com.registration.Points.VALID_PASSWORD

@Unroll
class MailServiceSpec extends Specification {
    def builder = Mock(EmailBuilder)
    def sender = Mock(JavaMailSender)
    def message = Stub(MimeMessage)

    def mailService = [builder, sender] as MailServiceImpl
    def user = [VALID_EMAIL, VALID_PASSWORD] as User

    def "should create and send email"() {
        when:
        mailService.sendEmail(user)

        then:
        1 * builder.createEmail(user) >> message
        1 * sender.send(message)
    }

    def "should throw exception on null user"() {
        when:
        mailService.sendEmail(null)

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("User can't be null.")

        and:
        0 * builder._
        0 * sender._
    }
}
