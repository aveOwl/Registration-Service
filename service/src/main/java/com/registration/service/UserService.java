package com.registration.service;

import com.registration.model.User;

/**
 * The UserService interface defines all public business behaviours
 * for operations with {@link User} entities.
 */
public interface UserService {

    /**
     * Persists given User entity in the database.
     *
     * @param user a User entity to be persisted.
     */
    void create(User user);

    /**
     * Retrieves user entity from the database using given user email.
     *
     * @param email email of the user to be searched.
     * @return User entity with given email or null
     * if no User is associated with the given email.
     */
    User findByEmail(String email);

    /**
     * Updates given User entity in the database.
     *
     * @param user a User entity to be updated.
     */
    void update(User user);

    /**
     * Confirms user registration.
     *
     * @param email email of user to confirm.
     */
    void confirm(String email);
}
