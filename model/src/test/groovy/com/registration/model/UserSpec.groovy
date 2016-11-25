package com.registration.model

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation

import static com.registration.Points.*

@Unroll
class UserSpec extends Specification {

    def user = [] as User

    def "should have constraint violations on invalid data"() {
        given:
        user.email = email
        user.password = password

        when:
        def violations = validateClass(user)
        def messages = violations
                .stream()
                .map({ v -> v.message })
                .collect()

        then:
        violations.size() == violationsCount
        messages.containsAll(message)

        where:
        email         | password         | violationsCount | message
        VALID_EMAIL   | VALID_PASSWORD   | 0               | []
        INVALID_EMAIL | VALID_PASSWORD   | 1               | INVALID_EMAIL_MSG
        VALID_EMAIL   | INVALID_PASSWORD | 1               | INVALID_PASSWORD_MSG
        INVALID_EMAIL | INVALID_PASSWORD | 2               | [INVALID_EMAIL_MSG, INVALID_PASSWORD_MSG]
        ""            | ""               | 3               | [EMPTY_EMAIL_MSG, EMPTY_PASSWORD_MSG, INVALID_PASSWORD_MSG]
    }

    def validateClass(User user) {
        def factory = Validation.buildDefaultValidatorFactory()
        def validator = factory.getValidator()
        return validator.validate(user)
    }
}
