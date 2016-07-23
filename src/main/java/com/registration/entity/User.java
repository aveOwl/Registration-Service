package com.registration.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * User entity class.
 */
@Entity
@Table(name = "user")
public class User {

    /**
     * Regular Expression for password verification.
     */
    private static final String PASSWORD_REGEXP = "((?=(.*\\d){2})(?=.*[!]).{6,20})";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * Email must be valid according to
     * {@link Email} annotation.
     */
    @Column(name = "email")
    @NotEmpty(message = "Input your email address")
    @Email(message = "Email is invalid")
    private String email;

    /**
     * Password must contain:
     * - at least 2 digits.
     * - at least one "!" symbol.
     * - at least 6 at most 20 characters long.
     */
    @Column(name = "password")
    @NotEmpty(message = "Choose a password")
    @Pattern(regexp = PASSWORD_REGEXP,
             message = "Password is invalid")
    private String password;

    /**
     * Check user's registration confirm.
     */
    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    public User() {}

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

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
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isConfirmed=" + isConfirmed +
                '}';
    }
}
