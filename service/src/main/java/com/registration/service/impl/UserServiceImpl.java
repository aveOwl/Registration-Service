package com.registration.service.impl;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

@Service
public class UserServiceImpl implements UserService {
    /**
     * Logging system for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(final User user) {
        Assert.notNull(user);

        if (user.getId() != null) {
            LOG.error("Attempted to create a User object, but id attribute was not null.");
            throw new EntityExistsException(
                    "Cannot create new User with supplied id. The id attribute must be null.");
        }

        final User savedUser = userRepository.save(user);

        LOG.debug("Persisted user entity: {}", savedUser);
        return savedUser;
    }

    @Override
    public User findByEmail(final String email) {
        Assert.notNull(email);

        final User user = userRepository.findByEmail(email);

        LOG.debug("User entity: {} fetched by email: {}", user, email);
        return user;
    }

    @Override
    public User update(final User user) {
        Assert.notNull(user);

        if (user.getId() == null) {
            LOG.error("Attempted to update a User object, but id attribute was null.");
            throw new EntityNotFoundException("Cannot preform update. The id attribute cannot be null.");
        }

        final User updatedUser = userRepository.save(user);

        LOG.info("Updated user entity: {}", updatedUser);
        return updatedUser;
    }

    @Override
    public void confirm(final String code) {
        try {
            byte[] decodedData = Base64Utils.decodeFromString(code);

            // array contains user email and password
            // {'email', 'password'}
            final String[] data = new String(decodedData).split(":");

            final User user = findByEmail(data[0]);

            if (user != null) {
                user.setConfirmed(true);
                update(user);
                LOG.debug("User: {} is confirmed.", user);
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid confirmation link: {}", code);
            throw new NoResultException("Invalid confirmation link.");
        }
    }
}
