package com.registration.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.ANY)
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

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
