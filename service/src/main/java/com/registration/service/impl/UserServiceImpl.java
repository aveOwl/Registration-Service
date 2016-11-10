package com.registration.service.impl;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import com.registration.util.EmailDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private EmailDecoder emailDecoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EmailDecoder emailDecoder) {
        this.userRepository = userRepository;
        this.emailDecoder = emailDecoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final User user) {
        Assert.notNull(user, "User can't be null.");

        if (user.getId() != null) {
            LOG.error("Attempted to create a User object, but id attribute was not null.");
            throw new EntityExistsException(
                    "Cannot create new User with supplied id. The id attribute must be null.");
        }

        User savedUser = this.userRepository.save(user);
        LOG.debug("Persisted user entity: {}", savedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final User user) {
        Assert.notNull(user, "User can't be null.");

        if (user.getId() == null) {
            LOG.error("Attempted to update a User object, but id attribute was null.");
            throw new EntityNotFoundException("Cannot perform update. The id attribute can't be null.");
        }

        User updatedUser = this.userRepository.save(user);
        LOG.info("Updated user entity: {}", updatedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findByEmail(final String email) {
        Assert.notNull(email, "Email can't be null.");

        User user = this.userRepository.findByEmail(email);

        LOG.debug("User entity: {} fetched by email: {}", user, email);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirm(final String confirmationCode) {
        Assert.notNull(confirmationCode, "Confirmation code can't be null.");

        String email = this.emailDecoder.decode(confirmationCode);
        User user = this.findByEmail(email);

        user.setConfirmed(true);
        this.update(user);

        LOG.debug("User: {} is confirmed.", user);
    }
}
