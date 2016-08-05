package com.registration.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.registration.Points.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
public class UserTest {

    private User user = null;

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
        User valid = new User(VALID_EMAIL, VALID_PASSWORD);
        User invalid = new User(INVALID_EMAIL, INVALID_PASSWORD);

        assertThat(valid.equals(invalid), is(false));

        valid.setConfirmed(true);
        invalid.setConfirmed(true);

        assertThat(valid.equals(invalid), is(false));
    }

    @Test
    public void shouldCompareTwoUsersAsEquals() throws Exception {
        User valid1 = new User(VALID_EMAIL, VALID_PASSWORD);
        User valid2 = new User(VALID_EMAIL, VALID_PASSWORD);

        assertThat(valid1.equals(valid2), is(true));

        valid1.setConfirmed(true);
        valid2.setConfirmed(true);

        assertThat(valid1.equals(valid2), is(true));
    }
}