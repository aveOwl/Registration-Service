package com.registration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * User entity class.
 */
@Entity
public class User {

    /**
     * User email.
     */
    @Id
    @Column(name = "email")
    private String email;

    /**
     * User password.
     */
    @Column(name = "password")
    private String password;

    /**
     * Check user's registration confirm.
     */
    @Column(name = "is_confirmed")
    private boolean isConfirmed;


    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(final boolean confirmed) {
        isConfirmed = confirmed;
    }

    @Override
    public String toString() {
        return "[email: " + email + "] [password: " + password
                + "] [confirmed: " + isConfirmed + "]";
    }
}
