package org.hw.entity;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StateProvinceCrudManagementIT extends HibernateTestBase {

    @Test
    void shouldCreateStateProvince() {
        StateProvince newStateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-create");
        verifyStateProvinceIsPersisted(newStateProvince);
    }

    @Test
    void shouldReadStateProvince() {
        StateProvince newStateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-read");
        verifyStateProvinceIsPersisted(newStateProvince);
    }

    @Test
    void shouldUpdateStateProvince() {
        StateProvince newStateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-update");
        changeStateProvinceNameAndVerify(newStateProvince, "newStateProvinceName");
    }

    @Test
    void shouldDeleteStateProvince() {
        StateProvince newStateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-delete");
        deleteStateProvinceAndVerifyAbsence(newStateProvince);
    }

    @Test
    void shouldCreateStateProvinceWithCountry() {
        Country existingCountry = testEntityGenerator.createAndPersistCountry("France", "FR");
        StateProvince newStateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes", existingCountry);

        verifyStateProvinceWithCountry(newStateProvince, "France", "FR");
    }

    @Test
    void shouldCreateStateProvinceWithMultipleCities() {
        Country existingCountry = testEntityGenerator.createAndPersistCountry("France", "FR");
        StateProvince newStateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes", existingCountry);

        City firstCity = testEntityGenerator.createAndPersistCity("Lyon", newStateProvince);
        City secondCity = testEntityGenerator.createAndPersistCity("Grenoble", newStateProvince);

        newStateProvince.getCities().add(firstCity);
        newStateProvince.getCities().add(secondCity);

        verifyStateProvinceWithCities(newStateProvince, "Lyon", "Grenoble");
    }

    private void verifyStateProvinceIsPersisted(StateProvince stateProvince) {
        refreshSession();

        StateProvince retrievedStateProvince = session.get(StateProvince.class, stateProvince.getId());
        assertNotNull(retrievedStateProvince);
        assertEquals(retrievedStateProvince, stateProvince);
    }

    private void changeStateProvinceNameAndVerify(StateProvince stateProvince, String newStateProvinceName) {
        refreshSession();

        stateProvince.setName(newStateProvinceName);
        session.merge(stateProvince);
        refreshSession();

        StateProvince updatedStateProvince = session.get(StateProvince.class, stateProvince.getId());
        assertNotNull(updatedStateProvince);
        assertEquals(newStateProvinceName, updatedStateProvince.getName());
    }

    private void deleteStateProvinceAndVerifyAbsence(StateProvince StateProvince) {
        refreshSession();

        session.remove(StateProvince);
        refreshSession();

        StateProvince deletedStateProvince = session.get(StateProvince.class, StateProvince.getId());
        assertNull(deletedStateProvince);
    }

    private void verifyStateProvinceWithCountry(StateProvince stateProvince, String expectedCountryName, String expectedCountryCode) {
        refreshSession();

        StateProvince retrievedStateProvince = session.get(StateProvince.class, stateProvince.getId());
        assertNotNull(retrievedStateProvince);
        assertNotNull(retrievedStateProvince.getCountry());
        assertEquals(expectedCountryName, retrievedStateProvince.getCountry().getName());
        assertEquals(expectedCountryCode, retrievedStateProvince.getCountry().getCode());
    }

    private void verifyStateProvinceWithCities(StateProvince stateProvince, String... expectedCityNames) {
        refreshSession();

        StateProvince retrievedStateProvince = session.get(StateProvince.class, stateProvince.getId());
        assertNotNull(retrievedStateProvince);
        assertEquals(expectedCityNames.length, retrievedStateProvince.getCities().size());
        assertTrue(retrievedStateProvince.getCities().stream()
                .map(City::getName)
                .collect(Collectors.toSet())
                .containsAll(Arrays.asList(expectedCityNames)));
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
