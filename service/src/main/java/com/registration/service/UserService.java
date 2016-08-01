package com.registration.service;

import com.registration.model.User;
import com.registration.service.impl.UserServiceImpl;

/**
 * <p>
 *     The UserService interface defines all public business behaviours
 *     for operations with {@link User} entities.
 * </p>
 * <p>
 *     This interface should be injected into MailService clients, not the
 *     {@link UserServiceImpl} class.
 * </p>
 */
public interface UserService {

    /**
     * Persists a User entity in the database.
     * @param user a User object to be persisted.
     * @return a persisted User object or <code>null</code>
     * if error occurred.
     */
    User create(User user);

    /**
     * Searches for user with given email.
     * @param email email to find user.
     * @return {@link User} object with given email or <code>null</code>
     * if no {@link User} is associated with given email.
     */
    User findByEmail(String email);

    /**
     * Updates a User entity in the database.
     * @param user a User object to be updated.
     * @return user with updated data, or <code>null</code>
     * if no such User object exists in the database.
     */
    User update(User user);

    /**
     * Confirms user registration.
     * Set flag 'is_confirmed' to true and
     * updates user in database.
     * @param email email of user to confirm.
     */
    void confirm(String email);
}
