package org.hw.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BrandCrudManagementIT extends HibernateTestBase {

    @Test
    void shouldCreateBrand() {
        Brand newBrand = testEntityGenerator.createAndPersistBrand("Arla", "http://logos.com/arla-logo.jpg");
        verifyBrandIsPersisted(newBrand);
    }

    @Test
    void shouldReadBrand() {
        Brand newBrand = testEntityGenerator.createAndPersistBrand("Arla", "http://logos.com/arla-logo.jpg");
        verifyBrandIsPersisted(newBrand);
    }

    @Test
    void shouldUpdateBrand() {
        Brand newBrand = testEntityGenerator.createAndPersistBrand("Arla", "http://logos.com/arla-logo.jpg");
        changeBrandNameAndVerify(newBrand, "Arla Drinks");
    }

    @Test
    void shouldDeleteBrand() {
        Brand newBrand = testEntityGenerator.createAndPersistBrand("Arla", "http://logos.com/arla-logo.jpg");
        deleteBrandAndVerifyAbsence(newBrand);
    }

    private void verifyBrandIsPersisted(Brand brand) {
        refreshSession();

        Brand retrievedBrand = session.get(Brand.class, brand.getId());
        assertNotNull(retrievedBrand);
        assertEquals(retrievedBrand, brand);
        assertEquals(brand.getName(), retrievedBrand.getName());
        assertEquals(brand.getLogo(), retrievedBrand.getLogo());
    }

    private void changeBrandNameAndVerify(Brand brand, String newBrandName) {
        refreshSession();

        brand.setName(newBrandName);
        session.merge(brand);
        refreshSession();

        Brand updatedBrand = session.get(Brand.class, brand.getId());
        assertNotNull(updatedBrand);
        assertEquals(newBrandName, updatedBrand.getName());
    }

    private void deleteBrandAndVerifyAbsence(Brand brand) {
        refreshSession();

        session.remove(brand);
        refreshSession();

        Brand deletedBrand = session.get(Brand.class, brand.getId());
        assertNull(deletedBrand);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}

