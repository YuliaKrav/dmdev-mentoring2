package org.hw.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductAnalogCrudManagementIT extends HibernateTestBase {

    private Product cocaCola;
    private Product pepsiCo;
    private Product fanta;

    @BeforeEach
    public void setUp() {
        super.setUp();
        createTestProductAnalogs();
    }

    @Test
    void shouldCreateAndReadProductAnalog() {
        ProductAnalog newProductAnalog = testEntityGenerator.createAndPersistProductAnalog(cocaCola, pepsiCo);
        verifyProductAnalogIsPersisted(newProductAnalog);
    }

    @Test
    void shouldUpdateProductAnalog() {
        ProductAnalog newProductAnalog = testEntityGenerator.createAndPersistProductAnalog(cocaCola, pepsiCo);
        changeProductAnalogProductAndVerify(newProductAnalog, fanta);
    }

    @Test
    void shouldDeleteProductAnalog() {
        ProductAnalog newProductAnalog = testEntityGenerator.createAndPersistProductAnalog(cocaCola, pepsiCo);
        deleteProductAnalogAndVerifyAbsence(newProductAnalog);
    }

    private void createTestProductAnalogs() {
        ProductCategory beverageCategory = testEntityGenerator.createAndPersistProductCategory("Beverages");
        Country country = testEntityGenerator.createAndPersistCountry("USA", "US");

       cocaCola = testEntityGenerator.createAndPersistProduct("Coca-Cola", "COKE100",
                "Classic Coca-Cola beverage", "Classic Coca-Cola beverage manufactured by The Coca-Cola Company.",
                new BigDecimal("1.99"), "http://images.com/coca-cola.jpg", 100, LocalDateTime.now(),
                LocalDateTime.now(), beverageCategory, null, country);
        pepsiCo = testEntityGenerator.createAndPersistProduct("PepsiCo", "PEPSI200",
                "Classic Pepsi beverage", "Classic PepsiCo beverage manufactured by PepsiCo.",
                new BigDecimal("1.99"), "http://images.com/pepsi.jpg", 100, LocalDateTime.now(),
                LocalDateTime.now(), beverageCategory, null, country);
        fanta = testEntityGenerator.createAndPersistProduct("Fanta", "FNT300",
                "Classic Fanta beverage", "Classic Fanta beverage manufactured by The Coca-Cola Company.",
                new BigDecimal("1.99"), "http://images.com/fanta.jpg", 150, LocalDateTime.now(),
                LocalDateTime.now(), beverageCategory, null, country);
    }

    private void verifyProductAnalogIsPersisted(ProductAnalog productAnalog) {
        refreshSession();

        ProductAnalog retrievedProductAnalog = session.get(ProductAnalog.class, productAnalog.getId());
        assertNotNull(retrievedProductAnalog);
        assertEquals(retrievedProductAnalog, productAnalog);
    }

    private void changeProductAnalogProductAndVerify(ProductAnalog productAnalog, Product newProductAnalog) {
        refreshSession();

        productAnalog.setAnalogProduct(newProductAnalog);
        session.merge(productAnalog);
        refreshSession();

        ProductAnalog updatedProductAnalog = session.get(ProductAnalog.class, productAnalog.getId());
        assertNotNull(updatedProductAnalog);
        assertEquals(newProductAnalog.getId(), updatedProductAnalog.getAnalogProduct().getId());
    }

    private void deleteProductAnalogAndVerifyAbsence(ProductAnalog productAnalog) {
        refreshSession();

        session.remove(productAnalog);
        refreshSession();

        ProductAnalog deletedProductAnalog = session.get(ProductAnalog.class, productAnalog.getId());
        assertNull(deletedProductAnalog);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
