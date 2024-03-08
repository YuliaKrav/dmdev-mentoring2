package org.hw.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RoleCrudManagementIT extends HibernateTestBase {

    private Role newRole;

    @BeforeEach
    void init() {
        newRole = testEntityGenerator.createAndPersistRole(RoleName.ADMIN);
    }

    @Test
    void shouldCreateAndReadRole() {
        verifyRoleIsPersisted(newRole);
    }

    @Test
    void shouldUpdateRole() {
        changeRoleNameAndVerify(newRole, RoleName.USER);
    }

    @Test
    void shouldDeleteRole() {
        deleteRoleAndVerifyAbsence(newRole);
    }

    private void verifyRoleIsPersisted(Role role) {
        refreshSession();

        Role retrievedRole = session.get(Role.class, role.getId());
        assertNotNull(retrievedRole);
        assertEquals(retrievedRole.getName(), role.getName());
    }

    private void changeRoleNameAndVerify(Role role, RoleName newRoleName) {
        refreshSession();

        role.setName(newRoleName);
        session.merge(role);
        refreshSession();

        Role updatedRole = session.get(Role.class, role.getId());
        assertNotNull(updatedRole);
        assertEquals(newRoleName, updatedRole.getName());
    }

    private void deleteRoleAndVerifyAbsence(Role role) {
        refreshSession();

        session.remove(role);
        refreshSession();

        Role deletedRole = session.get(Role.class, role.getId());
        assertNull(deletedRole);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}

