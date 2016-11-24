package com.registration.repository

import com.registration.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static com.registration.Points.*

@ContextConfiguration(classes = UserRepository)
@DataJpaTest(showSql = true)
@EntityScan(basePackages = "com.registration.model")
class UserRepositorySpec extends Specification {
    @Autowired
    UserRepository userRepository
    @Autowired
    TestEntityManager entityManager

    def user = [VALID_EMAIL, VALID_PASSWORD] as User

    def "should persist user"() {
        given:
        entityManager.persistAndFlush(user)

        when:
        def fetchedUser = userRepository.findByEmail(VALID_EMAIL)

        then:
        fetchedUser.email == VALID_EMAIL
        fetchedUser.password == VALID_PASSWORD
    }

    def "should update user"() {
        given:
        entityManager.persistAndFlush(user)
        user.email = "updated@email"

        when:
        def updatedUser = userRepository.save(user)

        then:
        updatedUser.email == "updated@email"
        updatedUser.password == VALID_PASSWORD
    }

    def "should delete user"() {
        given:
        entityManager.persistAndFlush(user)

        when:
        userRepository.delete(user)

        then:
        userRepository.findByEmail(VALID_EMAIL) == null
    }

    def "should return null if user does not exists"() {
        when:
        def fetchedUser = userRepository.findByEmail(INVALID_EMAIL)

        then:
        fetchedUser == null
    }
}
