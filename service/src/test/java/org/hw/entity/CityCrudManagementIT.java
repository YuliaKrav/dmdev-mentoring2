package org.hw.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CityCrudManagementIT extends HibernateTestBase {

    @Test
    void shouldCreateAndReadCity() {
        City newCity = testEntityGenerator.createAndPersistCity("Paris");
        verifyCityIsPersisted(newCity);
    }

    @Test
    void shouldUpdateUser() {
        City newCity = testEntityGenerator.createAndPersistCity("Paris");
        changeCityNameAndVerify(newCity, "newCityName");
    }

    @Test
    void shouldDeleteUser() {
        City newCity = testEntityGenerator.createAndPersistCity("Paris");
        deleteCityAndVerifyAbsence(newCity);
    }

    private void verifyCityIsPersisted(City city) {
        refreshSession();

        City retrievedCity = session.get(City.class, city.getId());
        assertNotNull(retrievedCity);
        assertEquals(retrievedCity, city);
    }

    private void changeCityNameAndVerify(City city, String newCityName) {
        refreshSession();

        city.setName(newCityName);
        session.merge(city);
        refreshSession();

        City updatedCity = session.get(City.class, city.getId());
        assertNotNull(updatedCity);
        assertEquals(newCityName, updatedCity.getName());
    }

    private void deleteCityAndVerifyAbsence(City city) {
        refreshSession();

        session.remove(city);
        refreshSession();

        City deletedCity = session.get(City.class, city.getId());
        assertNull(deletedCity);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}

