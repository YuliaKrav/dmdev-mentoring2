package org.hw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hw.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserManagementTest {

    private SessionFactory sessionFactory;
    private final String emailTest = "email@test.com";

    @BeforeEach
    void setUp() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterEach
    void tearDown() {
        deleteUserByEmail(emailTest);

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void testUserCreation() {

        assertNull(getUserByEmail("email@test.com"));

        addUser("Test", "Testov", emailTest, "testpass", true);

        User user = getUserByEmail(emailTest);
        assertNotNull(user);
        assertEquals(emailTest, user.getEmail());
        assertEquals("Test", user.getFirstName());
        assertEquals("Testov", user.getLastName());
        assertTrue(user.getEnabled());
    }

    private void addUser(String firstName, String lastName, String email, String password, boolean enabled) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            User newUser = new User();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setEnabled(enabled);

            session.save(newUser);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User getUserByEmail(String email) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            user = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    private void deleteUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User userToDelete = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            if (userToDelete != null) {
                session.delete(userToDelete);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
