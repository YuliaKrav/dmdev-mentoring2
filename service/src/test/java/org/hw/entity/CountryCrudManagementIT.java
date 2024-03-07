package org.hw.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CountryCrudManagementIT extends HibernateTestBase {

    @Test
    void shouldCreateCountry() {
        Country newCountry = testEntityGenerator.createAndPersistCountry("France-create", "FR");
        verifyCountryIsPersisted(newCountry);
    }

    @Test
    void shouldReadCountry() {
        Country newCountry = testEntityGenerator.createAndPersistCountry("France-read", "FR");
        verifyCountryIsPersisted(newCountry);
    }

    @Test
    void shouldUpdateCountry() {
        Country newCountry = testEntityGenerator.createAndPersistCountry("France-update", "FR");
        changeCountryNameAndVerify(newCountry, "newCountryName");
    }

    @Test
    void shouldDeleteCountry() {
        Country newCountry = testEntityGenerator.createAndPersistCountry("France-delete", "FR");
        deleteCountryAndVerifyAbsence(newCountry);
    }

    private void verifyCountryIsPersisted(Country country) {
        refreshSession();

        Country retrievedCountry = session.get(Country.class, country.getId());
        assertNotNull(retrievedCountry);
        assertEquals(retrievedCountry, country);
    }

    private void changeCountryNameAndVerify(Country country, String newCountryName) {
        refreshSession();

        country.setName(newCountryName);
        session.merge(country);
        refreshSession();

        Country updatedCountry = session.get(Country.class, country.getId());
        assertNotNull(updatedCountry);
        assertEquals(newCountryName, updatedCountry.getName());
    }

    private void deleteCountryAndVerifyAbsence(Country country) {
        refreshSession();

        session.remove(country);
        refreshSession();

        Country deletedCountry = session.get(Country.class, country.getId());
        assertNull(deletedCountry);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
