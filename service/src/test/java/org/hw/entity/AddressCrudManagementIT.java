package org.hw.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddressCrudManagementIT extends HibernateTestBase {

    @ParameterizedTest
    @CsvSource({
            "France, FR, Auvergne-Rhône-Alpes, Lyon, Rue de la République, 69002",
            "France, FR, Île-de-France, Paris, Champs-Élysées, 75008",
            "Germany, DE, Bavaria, Munich, Marienplatz, 80331"
    })
    void shouldCreateAddressWithDifferentData(String countryName, String countryCode, String stateProvinceName, String cityName, String street, String zipCode) {
        Country country = testEntityGenerator.createAndPersistCountry(countryName, countryCode);
        StateProvince stateProvince = testEntityGenerator.createAndPersistStateProvince(stateProvinceName);
        City city = testEntityGenerator.createAndPersistCity(cityName);
        Address newAddress = testEntityGenerator.createAndPersistAddress(street, zipCode, city, stateProvince, country);
        verifyAddressIsPersisted(newAddress, country, stateProvince, city, street, zipCode);
    }

    @Test
    void shouldReadAddress() {
        Country country = testEntityGenerator.createAndPersistCountry("France", "FR");
        StateProvince stateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-create");
        City city = testEntityGenerator.createAndPersistCity("Lyon");
        Address newAddress = testEntityGenerator.createAndPersistAddress("Rue de la République", "69002", city, stateProvince, country);
        verifyAddressIsPersisted(newAddress, country, stateProvince, city, "Rue de la République", "69002");
    }

    @Test
    void shouldUpdateAddress() {
        Country country = testEntityGenerator.createAndPersistCountry("France", "FR");
        StateProvince stateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-create");
        City city = testEntityGenerator.createAndPersistCity("Lyon");
        Address newAddress = testEntityGenerator.createAndPersistAddress("Rue de la République", "69002", city, stateProvince, country);
        City newCity = testEntityGenerator.createAndPersistCity("Grenoble");
        changeAddressCityAndVerify(newAddress, newCity);
    }

    @Test
    void shouldDeleteAddress() {
        Country country = testEntityGenerator.createAndPersistCountry("France", "FR");
        StateProvince stateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-create");
        City city = testEntityGenerator.createAndPersistCity("Lyon");
        Address newAddress = testEntityGenerator.createAndPersistAddress("Rue de la République", "69002", city, stateProvince, country);
        deleteAddressAndVerifyAbsence(newAddress);
    }

    private void verifyAddressIsPersisted(Address address, Country country, StateProvince stateProvince, City city, String street, String zipCode) {
        refreshSession();

        Address retrievedAddress = session.get(Address.class, address.getId());
        assertNotNull(retrievedAddress);
        assertEquals(street, retrievedAddress.getStreet());
        assertEquals(zipCode, retrievedAddress.getZipCode());
        assertEquals(city.getName(), retrievedAddress.getCity().getName());
        assertEquals(stateProvince.getName(), retrievedAddress.getStateProvince().getName());
        assertEquals(country.getName(), retrievedAddress.getCountry().getName());
    }

    private void changeAddressCityAndVerify(Address Address, City city) {
        refreshSession();

        Address.setCity(city);
        session.merge(Address);
        refreshSession();

        Address updatedAddress = session.get(Address.class, Address.getId());
        assertNotNull(updatedAddress);
        assertEquals(city.getName(), updatedAddress.getCity().getName());
    }

    private void deleteAddressAndVerifyAbsence(Address address) {
        refreshSession();

        session.remove(address);
        refreshSession();

        Address deletedAddress = session.get(Address.class, address.getId());
        assertNull(deletedAddress);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
