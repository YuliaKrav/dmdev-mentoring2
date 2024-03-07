package org.hw.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserCrudManagementIT extends HibernateTestBase {

    @Test
    void shouldCreateUser() {
        User newUser = testEntityGenerator.createAndPersistUser("Eva", "Moon", "eva.moon@create-test.com", "securePass", true);
        verifyUserIsPersisted(newUser);
    }

    @Test
    void shouldReadUser() {
        User newUser = testEntityGenerator.createAndPersistUser("Eva", "Moon", "eva.moon@read-test.com", "securePass", true);
        verifyUserIsPersisted(newUser);
    }

    @Test
    void shouldUpdateUser() {
        User newUser = testEntityGenerator.createAndPersistUser("Eva", "Moon", "eva.moon@update-test.com", "securePass", true);
        changeUserLastNameAndVerify(newUser, "Sunny");
    }

    @Test
    void shouldDeleteUser() {
        User newUser = testEntityGenerator.createAndPersistUser("Eva", "Moon", "eva.moon@delete-test.com", "securePass", true);
        deleteUserAndVerifyAbsence(newUser);
    }

    private void verifyUserIsPersisted(User user) {
        refreshSession();

        User retrievedUser = session.get(User.class, user.getId());
        assertNotNull(retrievedUser);
        assertEquals(retrievedUser, user);
    }

    private void changeUserLastNameAndVerify(User user, String newLastName) {
        refreshSession();

        user.setLastName(newLastName);
        session.merge(user);
        refreshSession();

        User updatedUser = session.get(User.class, user.getId());
        assertNotNull(updatedUser);
        assertEquals(newLastName, updatedUser.getLastName());
    }

    private void deleteUserAndVerifyAbsence(User user) {
        refreshSession();

        session.remove(user);
        refreshSession();

        User deletedUser = session.get(User.class, user.getId());
        assertNull(deletedUser);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
