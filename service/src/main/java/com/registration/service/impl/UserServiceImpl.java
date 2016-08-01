package com.registration.service.impl;

import com.registration.model.User;
import com.registration.repository.UserRepository;
import com.registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

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

        final String email = user.getEmail();

        if (findByEmail(email) != null) {
            LOG.error("Attempt to create a User object, but email was taken.");
            throw new EntityExistsException("Email is taken.");
        }

        LOG.debug("Creating user: {}", user);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User findByEmail(final String email) {
        Assert.notNull(email);

        LOG.debug("Fetching user entity by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public User update(final User user) {
        Assert.notNull(user);

        if (user.getId() == null) {
            LOG.error("Can't preform update id cannot be null: {}", user);
            throw new EntityNotFoundException("No such user in the database.");
        }

        LOG.debug("Updating user: {}", user);
        return userRepository.saveAndFlush(user);
    }
}
