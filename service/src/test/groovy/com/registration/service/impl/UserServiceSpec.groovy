package com.registration.service.impl

import com.registration.model.User
import com.registration.repository.UserRepository
import com.registration.util.EmailDecoder
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import javax.persistence.NoResultException

@Unroll
class UserServiceSpec extends Specification {
    def repository = Mock(UserRepository)
    def decoder = Mock(EmailDecoder)

    def service = [repository, decoder] as UserServiceImpl
    def user = ["test@gmail.com", "testPassword"] as User

    def "should create user with null id"() {
        given:
        user.setId(null)

        when:
        service.create(user)

        then:
        with(repository) {
            1 * save({ it.getEmail() == user.getEmail() & it.getPassword() == user.getPassword() }) >> user
            0 * _
        }
    }

    def "should not create user which already has an id"() {
        given:
        user.setId(1L)

        when:
        service.create(user)

        then:
        EntityExistsException e = thrown()
        e.message.contains("Cannot create new User with supplied id. The id attribute must be null.")

        and:
        0 * repository._
    }

    def "should not create null user"() {
        when:
        service.create(null)

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("User can't be null.")

        and:
        0 * repository._
    }

    def "should update user which already has an id"() {
        given:
        user.setId(2L)

        when:
        service.update(user)

        then:
        with(repository) {
            1 * save({ it.getEmail() == user.getEmail() & it.getPassword() == user.getPassword() }) >> user
            0 * _
        }
    }

    def "should not update user with null id"() {
        given:
        user.setId(null)

        when:
        service.update(user)

        then:
        EntityNotFoundException e = thrown()
        e.message.contains("Cannot perform update. The id attribute can't be null.")

        and:
        0 * repository._
    }

    def "should not update null user"() {
        when:
        service.update(null)

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("User can't be null.")

        and:
        0 * repository._
    }

    def "should find user by email"() {
        when:
        service.findByEmail(!null as String)

        then:
        with(repository) {
            1 * findByEmail(!null as String) >> user
            0 * _
        }
    }

    def "should not find user by null email"() {
        when:
        service.findByEmail(null)

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("Email can't be null.")

        and:
        0 * repository._
    }

    def "should confirm user on valid confirmation code"() {
        given:
        user.setId(5L)

        when:
        service.confirm(!null as String)

        then:
        1 * decoder.decode(!null as String) >> user.getEmail()
        1 * repository.findByEmail(user.getEmail()) >> user

        and:
        with(repository) {
            1 * save({ it.isConfirmed() == true })
            0 * _
        }
    }

    def "should not confirm user on null confirmation code"() {
        when:
        service.confirm(null)

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("Confirmation code can't be null.")

        and:
        0 * repository._
    }

    def "should not confirm user if failed to decode confirmation code"() {
        when:
        service.confirm(!null as String)

        then:
        1 * decoder.decode(!null as String) >> { throw new NoResultException() }

        and:
        thrown(NoResultException)

        and:
        with(repository) {
            0 * _
        }
    }
}
