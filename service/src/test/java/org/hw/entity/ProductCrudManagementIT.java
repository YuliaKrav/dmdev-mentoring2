package org.hw.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductCrudManagementIT extends HibernateTestBase {

    @Test
    void shouldCreateProduct() {
        ProductCategory category = testEntityGenerator.createAndPersistProductCategory("Dairy Products");
        Brand brand = testEntityGenerator.createAndPersistBrand("Arla", "http://logos.com/arla-logo.jpg");
        Country country = testEntityGenerator.createAndPersistCountry("Denmark", "DK");

        Product newProduct = testEntityGenerator.createAndPersistProduct("Arla LactoFree Milk", "ArlaLF100",
                "Lactose-Free Milk", "Arla LactoFree Milk is a completely natural milk without lactose.",
                new BigDecimal("2.99"), "http://images.com/arla-lactofree.jpg", 100, LocalDateTime.now(),
                LocalDateTime.now(), category, brand, country);

        verifyProductIsPersisted(newProduct);
    }

    @Test
    void shouldReadProduct() {
        ProductCategory category = testEntityGenerator.createAndPersistProductCategory("Beverages");
        Brand brand = testEntityGenerator.createAndPersistBrand("Coca-Cola", "http://logos.com/cocacola-logo.jpg");
        Country country = testEntityGenerator.createAndPersistCountry("United States", "US");

        Product newProduct = testEntityGenerator.createAndPersistProduct("Coca-Cola Zero", "CCZ100",
                "Zero Sugar Coca-Cola", " Coca-Cola with zero sugar and zero calories.",
                new BigDecimal("1.59"), "http://images.com/cocacola-zero.jpg", 200, LocalDateTime.now(),
                LocalDateTime.now(), category, brand, country);

        verifyProductIsPersisted(newProduct);
    }

    @Test
    void shouldUpdateProduct() {
        ProductCategory category = testEntityGenerator.createAndPersistProductCategory("Beverages");
        Brand brand = testEntityGenerator.createAndPersistBrand("Sprite", "http://logos.com/sprite-logo.jpg");
        Country country = testEntityGenerator.createAndPersistCountry("United States", "US");

        Product newProduct = testEntityGenerator.createAndPersistProduct("Sprite", "SPR100",
                "Lemon-Lime Sprite", "A sparkle refreshing drink with a lemon-lime taste.",
                new BigDecimal("1.75"), "http://images.com/sprite.jpg", 120, LocalDateTime.now(),
                LocalDateTime.now(), category, brand, country);

        changeProductNameAndVerify(newProduct, "Sprite Lite");
    }

    @Test
    void shouldDeleteProduct() {
        ProductCategory category = testEntityGenerator.createAndPersistProductCategory("Beverages");
        Brand brand = testEntityGenerator.createAndPersistBrand("Sprite", "http://logos.com/sprite-logo.jpg");
        Country country = testEntityGenerator.createAndPersistCountry("United States", "US");

        Product newProduct = testEntityGenerator.createAndPersistProduct("Sprite", "SPR100",
                "Lemon-Lime Sprite", "A sparkle refreshing drink with a lemon-lime taste.",
                new BigDecimal("1.75"), "http://images.com/sprite.jpg", 120, LocalDateTime.now(),
                LocalDateTime.now(), category, brand, country);

        deleteProductAndVerifyAbsence(newProduct);
    }

    private void verifyProductIsPersisted(Product product) {
        refreshSession();

        Product retrievedProduct = session.get(Product.class, product.getId());
        assertNotNull(retrievedProduct);
        assertEquals(product.getName(), retrievedProduct.getName());
        assertEquals(product.getSku(), retrievedProduct.getSku());
        assertEquals(product.getUnitPrice(), retrievedProduct.getUnitPrice());
        assertEquals(product.getUnitsInStock(), retrievedProduct.getUnitsInStock());
        assertEquals(product.getCategory().getCategoryName(), retrievedProduct.getCategory().getCategoryName());
        assertEquals(product.getBrand().getName(), retrievedProduct.getBrand().getName());
        assertEquals(product.getCountryOfOrigin().getName(), retrievedProduct.getCountryOfOrigin().getName());
    }

    private void changeProductNameAndVerify(Product product, String newProductName) {
        refreshSession();

        product.setName(newProductName);
        session.merge(product);
        refreshSession();

        Product updatedProduct = session.get(Product.class, product.getId());
        assertNotNull(updatedProduct);
        assertEquals(newProductName, updatedProduct.getName());
    }

    private void deleteProductAndVerifyAbsence(Product product) {
        refreshSession();

        session.remove(product);
        refreshSession();

        Product deletedProduct = session.get(Product.class, product.getId());
        assertNull(deletedProduct);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
