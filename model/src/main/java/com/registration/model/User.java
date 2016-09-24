package com.registration.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import static com.registration.Points.*;

/**
 * User entity class.
 */
@Entity
@Table(name = "users")
@Component
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
    @NotEmpty(message = EMPTY_EMAIL_MSG)
    @Email(message = INVALID_EMAIL_MSG)
    @Column(name = "email")
    private String email;

    /**
     * Password must contain:
     * - at least 2 digits.
     * - at least one "!" symbol.
     * - at least 6 at most 20 characters long.
     */
    @NotEmpty(message = EMPTY_PASSWORD_MSG)
    @Pattern(regexp = PASSWORD_REGEXP, message = INVALID_PASSWORD_MSG)
    @Column(name = "password")
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

    /**already exists
     * Compares this user to the specified user.
     * @param other the other user.
     * @return true if this user equals other; false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        User that = (User) other;
        return (this.email.equals(that.email)) &&
               (this.password.equals(that.password) &&
                this.isConfirmed == that.isConfirmed);
    }

    /**
     * Returns a hash code for this user.
     * @return a hash code for this user.
     */
    @Override
    public int hashCode() {
        int hash = 17;
        if (email != null && password != null) {
            hash = 31 * hash + email.hashCode();
            hash = 31 * hash + password.hashCode();
        }
        return hash;
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
