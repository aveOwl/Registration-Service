package com.registration.repository;

import com.registration.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.registration.Points.VALID_EMAIL;
import static com.registration.Points.VALID_PASSWORD;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = UserRepository.class)
@EntityScan(basePackages = "com.registration.model")
public class UserRepositoryTest {

    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        user = new User(VALID_EMAIL, VALID_PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void shouldCreateUser() throws Exception {
        entityManager.persistAndFlush(user);

        User user = userRepository.findByEmail(VALID_EMAIL);

        assertThat("valid user email", user.getEmail(), is(VALID_EMAIL));
        assertThat("valid user password", user.getPassword(), is(VALID_PASSWORD));
    }

    @Test
    public void shouldFindByEmail() throws Exception {
        entityManager.persistAndFlush(user);

        User byEmail = userRepository.findByEmail(VALID_EMAIL);

        assertThat("valid user", byEmail, is(user));
    }

    @Test
    public void shouldReturnNullOnNonExistingUser() throws Exception {
        User byEmail = userRepository.findByEmail(VALID_EMAIL);

        assertNull("should be null value if user don't exist", byEmail);
    }
}