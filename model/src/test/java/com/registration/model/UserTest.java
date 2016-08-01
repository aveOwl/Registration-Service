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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
    public void shouldHaveTwoViolationsOnEmptyData() throws Exception {
        user.setEmail("");
        user.setPassword("");

        Set<ConstraintViolation<User>> violations = validateClass(user);

        assertThat(violations.size(), is(2));
    }

    @Test
    public void shouldHavePasswordViolation() throws Exception {
        user.setEmail(VALID_EMAIL);
        user.setPassword(INVALID_PASSWORD);

        Set<ConstraintViolation<User>> violations = validateClass(user);

        assertThat(violations.size(), is(1));
        for (ConstraintViolation<User> violation : violations) {
            assertThat(violation.getMessage(), is("Password is invalid"));
        }
    }

    @Test
    public void shouldHaveEmailViolation() throws Exception {
        user.setEmail(INVALID_EMAIL);
        user.setPassword(VALID_PASSWORD);

        Set<ConstraintViolation<User>> violations = validateClass(user);

        assertThat(violations.size(), is(1));
        for (ConstraintViolation<User> violation : violations) {
            assertThat(violation.getMessage(), is("Email is invalid"));
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
}
