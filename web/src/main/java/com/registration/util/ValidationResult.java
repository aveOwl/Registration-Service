package com.registration.util;

import com.registration.model.User;
import org.hibernate.validator.constraints.Email;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.constraints.Pattern;

/**
 * Class FormValidationResult provides response
 * to {@link User} entity form validation.
 */
public class ValidationResult {

    /**
     * If set to true user validation
     * was successful.
     */
    private boolean success;

    /**
     * True: user email composed correctly.
     * False: no email provided / provided email
     * does not match {@link Email} well formed email structure.
     */
    private boolean invalidEmail;

    /**
     * True: user password is valid.
     * False: provided password does not match
     * password {@link Pattern} provided in {@link User} entity class.
     */
    private boolean invalidPassword;

    private String emailViolationMessage;

    private String passwordViolationMessage;

    /**
     * Constructs {@link ValidationResult} object with provided
     * {@link BindingResult} which contains information about
     * user validation according to constraints defined in {@link User} entity class.
     * @param bindingResult validation result.
     */
    public ValidationResult(final BindingResult bindingResult) {
        this.success = !bindingResult.hasFieldErrors();

        this.invalidEmail = bindingResult.hasFieldErrors("email");
        this.invalidPassword = bindingResult.hasFieldErrors("password");

        StringBuilder emailMessageBuilder = new StringBuilder();
        StringBuilder passwordMessageBuilder = new StringBuilder();

        // Build ConfirmationEmail Violation message.
        if (this.invalidEmail) {
            for (FieldError error : bindingResult.getFieldErrors("email")) {
                emailMessageBuilder.append(error.getDefaultMessage());
            }
        }
        this.emailViolationMessage = emailMessageBuilder.toString();

        // Build Password Violation message.
        if (this.invalidPassword) {
            for (FieldError error : bindingResult.getFieldErrors("password")) {
                passwordMessageBuilder.append(error.getDefaultMessage());
            }
        }
        this.passwordViolationMessage = passwordMessageBuilder.toString();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isInvalidEmail() {
        return this.invalidEmail;
    }

    public boolean isInvalidPassword() {
        return this.invalidPassword;
    }

    public String getEmailViolationMessage() {
        return this.emailViolationMessage;
    }

    public String getPasswordViolationMessage() {
        return this.passwordViolationMessage;
    }
}
