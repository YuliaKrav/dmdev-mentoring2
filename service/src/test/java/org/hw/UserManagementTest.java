package org.hw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hw.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserManagementTest {

    private SessionFactory sessionFactory;
    private Session session;
    private final String emailTest = "email@test.com";

    @BeforeAll
    void setUpClass() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        session = sessionFactory.openSession();
    }

    @AfterEach
    void tearDown() {
        if (session != null) {
            session.close();
        }
    }

    @AfterAll
    void tearDownClass() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void shouldCreateUserSuccessfully() {
        assertNull(getUserByEmail("email@test.com"));

        addUser("Test", "Testov", emailTest, "testpass", true);

        User user = getUserByEmail(emailTest);
        assertNotNull(user);
        assertEquals(emailTest, user.getEmail());
        assertEquals("Test", user.getFirstName());
        assertEquals("Testov", user.getLastName());
        assertTrue(user.getEnabled());

        deleteUserByEmail(emailTest);
    }

    private void addUser(String firstName, String lastName, String email, String password, boolean enabled) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            User newUser = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password(password)
                    .enabled(enabled).build();

            session.persist(newUser);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private User getUserByEmail(String email) {
        return session.createQuery("from User where email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    private void deleteUserByEmail(String email) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User userToDelete = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            if (userToDelete != null) {
                session.remove(userToDelete);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
