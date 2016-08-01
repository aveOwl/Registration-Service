package com.registration.service;

import com.registration.model.User;

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
}
