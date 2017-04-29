package com.registration.service.impl;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import com.registration.util.EmailDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailDecoder emailDecoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final User user) {
        Assert.notNull(user, "User can't be null.");

        if (user.getId() != null) {
            throw new EntityExistsException(
                    "Cannot create new User with supplied id. The id attribute must be null.");
        }
        val savedUser = this.userRepository.save(user);
        log.debug("Persisted user entity: {}", savedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final User user) {
        Assert.notNull(user, "User can't be null.");

        if (user.getId() == null) {
            throw new EntityNotFoundException("Cannot perform update. The id attribute can't be null.");
        }
        val updatedUser = this.userRepository.save(user);
        log.info("Updated user entity: {}", updatedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findByEmail(final String email) {
        Assert.notNull(email, "Email can't be null.");
        val user = this.userRepository.findByEmail(email);
        log.debug("User entity: {} fetched by email: {}", user, email);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirm(final String confirmationCode) {
        Assert.notNull(confirmationCode, "Confirmation code can't be null.");

        val email = this.emailDecoder.decode(confirmationCode);
        val user = this.findByEmail(email);

        user.setConfirmed(true);
        this.update(user);
        log.debug("User: {} is confirmed.", user);
    }
}
