package com.registration.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.registration.model.User.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class UserTest {

    private User user = null;
    private static final String VALID_EMAIL = "valid@domain.com";
    private static final String INVALID_EMAIL = "invalid!domain.com";
    private static final String VALID_PASSWORD = "pass!wo2r1";
    private static final String INVALID_PASSWORD = "password";

    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldHaveThreeViolationsOnEmptyData() throws Exception {
        user.setEmail("");
        user.setPassword("");

        Set<ConstraintViolation<User>> violations = validateClass(user);

        assertThat(violations.size(), is(3));
    }

    @Test
    public void shouldHavePasswordViolation() throws Exception {
        user.setEmail(VALID_EMAIL);
        user.setPassword(INVALID_PASSWORD);

        Set<ConstraintViolation<User>> violations = validateClass(user);

        assertThat(violations.size(), is(1));
        for (ConstraintViolation<User> violation : violations) {
            assertThat(violation.getMessage(), is(INVALID_PASSWORD_MSG));
        }
    }

    @Test
    public void shouldHaveEmailViolation() throws Exception {
        user.setEmail(INVALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        Set<ConstraintViolation<User>> violations = validateClass(user);

        assertThat(violations.size(), is(1));
        for (ConstraintViolation<User> violation : violations) {
            assertThat(violation.getMessage(), is(INVALID_EMAIL_MSG));
        }
    }

    @Test
    public void shouldHaveNoViolations() throws Exception {
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        Set<ConstraintViolation<User>> violations = validateClass(user);

        assertThat(violations.size(), is(0));
    }

    private Set<ConstraintViolation<User>> validateClass(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(user);
    }

    @Test
    public void shouldCompareTwoUserAsNotEquals() throws Exception {
        User valid1 = new User(VALID_EMAIL, VALID_PASSWORD);
        User valid2 = new User(VALID_EMAIL, VALID_PASSWORD);
        User invalid = new User(VALID_EMAIL, INVALID_PASSWORD);

        assertThat(valid1.getEmail().equals(invalid.getEmail()), is(true));
        assertThat(valid1.getPassword().equals(invalid.getPassword()), is(false));
        assertThat(valid1.equals(invalid), is(false));

        valid1.setConfirmed(true);
        assertThat(valid1.equals(valid2), is(false));
    }

    @Test
    public void shouldCompareTwoUsersAsEquals() throws Exception {
        User valid1 = new User(VALID_EMAIL, VALID_PASSWORD);
        User valid2 = new User(VALID_EMAIL, VALID_PASSWORD);

        assertThat(valid1.equals(valid2), is(true));
        assertThat(valid1.getEmail().equals(valid2.getEmail()), is(true));
        assertThat(valid1.getPassword().equals(valid2.getPassword()), is(true));
        assertThat(valid1.equals(valid2), is(true));
    }
}
