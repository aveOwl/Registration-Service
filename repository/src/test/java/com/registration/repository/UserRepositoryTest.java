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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.registration.Points.VALID_EMAIL;
import static com.registration.Points.VALID_PASSWORD;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = UserRepository.class)
@EntityScan(basePackages = "com.registration.model")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;

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
        this.entityManager.persistAndFlush(user);

        User user = this.userRepository.findByEmail(VALID_EMAIL);

        assertEquals(user.getEmail(), VALID_EMAIL);
        assertEquals(user.getPassword(), VALID_PASSWORD);
    }

    @Test
    public void shouldFindByEmail() throws Exception {
        this.entityManager.persistAndFlush(user);

        User byEmail = this.userRepository.findByEmail(VALID_EMAIL);

        assertEquals(user, byEmail);
    }

    @Test
    public void shouldReturnNullOnNonExistingUser() throws Exception {
        User byEmail = this.userRepository.findByEmail(VALID_EMAIL);

        assertEquals(null, byEmail);
    }
}