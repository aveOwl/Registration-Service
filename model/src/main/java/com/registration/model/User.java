package com.registration.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import static com.registration.Points.EMPTY_EMAIL_MSG;
import static com.registration.Points.EMPTY_PASSWORD_MSG;
import static com.registration.Points.INVALID_EMAIL_MSG;
import static com.registration.Points.INVALID_PASSWORD_MSG;

@Entity
@Table(name = "users")
public class User {

    /**
     * Password must be from 6 to 20 characters long.
     * Password must contain:
     * - at least 2 digits.
     * - at least one "!" symbol.
     */
    private static final String PASSWORD_REGEXP = "((?=(.*\\d){2})(?=.*[!]).{6,20})";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * Email must not be empty and be valid
     * according to {@link Email} annotation.
     */
    @NotEmpty(message = EMPTY_EMAIL_MSG)
    @Email(message = INVALID_EMAIL_MSG)
    @Column(name = "email")
    private String email;

    /**
     * Password must not be empty.
     */
    @NotEmpty(message = EMPTY_PASSWORD_MSG)
    @Pattern(regexp = PASSWORD_REGEXP, message = INVALID_PASSWORD_MSG)
    @Column(name = "password")
    private String password;

    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    public User() {
    }

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public boolean isConfirmed() {
        return this.isConfirmed;
    }

    public void setConfirmed(final boolean confirmed) {
        this.isConfirmed = confirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (isConfirmed != user.isConfirmed) return false;
        if (id != null ? ! id.equals(user.id) : user.id != null) return false;
        if (! email.equals(user.email)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (isConfirmed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("id: %s, email: %s, password: %s, isConfirmed: %s",
                this.id, this.email, this.password, this.isConfirmed);
    }
}
