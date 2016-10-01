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
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(final User user) {
        Assert.notNull(user);

        if (user.getId() != null) {
            LOG.error("Attempted to create a User object, but id attribute was not null.");
            throw new EntityExistsException(
                    "Cannot create new User with supplied id. The id attribute must be null.");
        }

        User savedUser = userRepository.save(user);
        LOG.debug("Persisted user entity: {}", savedUser);
    }

    @Override
    public void update(final User user) {
        Assert.notNull(user);

        if (user.getId() == null) {
            LOG.error("Attempted to update a User object, but id attribute was null.");
            throw new EntityNotFoundException("Cannot preform update. The id attribute cannot be null.");
        }

        User updatedUser = userRepository.save(user);
        LOG.info("Updated user entity: {}", updatedUser);
    }

    @Override
    public void confirm(final String code) {
        Assert.notNull(code);

        String email = this.decodeEmail(code);
        Optional<User> user = this.findByEmail(email);

        User confirmedUser = user.orElseThrow(() -> new NoResultException("Invalid confirmation link"));

        confirmedUser.setConfirmed(true);
        this.update(confirmedUser);

        LOG.debug("User: {} is confirmed.", confirmedUser);
    }

    private String decodeEmail(final String code) {
        try {
            byte[] decodedData = Base64Utils.decodeFromString(code);

            String[] data = new String(decodedData).split(":"); // {'email', 'password'}

            String email = data[0];

            return email;
        } catch (IllegalArgumentException e) {
            throw new NoResultException("Invalid confirmation link");
        }
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        Assert.notNull(email);

        User user = userRepository.findByEmail(email);

        LOG.debug("User entity: {} fetched by email: {}", user, email);
        return Optional.ofNullable(user);
    }
}
