package org.hw.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductCategoryCrudManagementIT extends HibernateTestBase {

    @Test
    void shouldCreateProductCategory() {
        ProductCategory newProductCategory = testEntityGenerator.createAndPersistProductCategory("Dairy Products");
        verifyProductCategoryIsPersisted(newProductCategory);
    }

    @Test
    void shouldReadProductCategory() {
        ProductCategory newProductCategory = testEntityGenerator.createAndPersistProductCategory("Dairy Products");
        verifyProductCategoryIsPersisted(newProductCategory);
    }

    @Test
    void shouldUpdateProductCategory() {
        ProductCategory newProductCategory = testEntityGenerator.createAndPersistProductCategory("Dairy Products");
        changeProductCategoryNameAndVerify(newProductCategory, "newProductCategoryName");
    }

    @Test
    void shouldDeleteProductCategory() {
        ProductCategory newProductCategory = testEntityGenerator.createAndPersistProductCategory("Dairy Products");
        deleteProductCategoryAndVerifyAbsence(newProductCategory);
    }

    private void verifyProductCategoryIsPersisted(ProductCategory productCategory) {
        refreshSession();

        ProductCategory retrievedProductCategory = session.get(ProductCategory.class, productCategory.getId());
        assertNotNull(retrievedProductCategory);
        assertEquals(retrievedProductCategory, productCategory);
        assertEquals(productCategory.getCategoryName(), retrievedProductCategory.getCategoryName());
    }

    private void changeProductCategoryNameAndVerify(ProductCategory productCategory, String newProductCategoryName) {
        refreshSession();

        productCategory.setCategoryName(newProductCategoryName);
        session.merge(productCategory);
        refreshSession();

        ProductCategory updatedProductCategory = session.get(ProductCategory.class, productCategory.getId());
        assertNotNull(updatedProductCategory);
        assertEquals(newProductCategoryName, updatedProductCategory.getCategoryName());
    }

    private void deleteProductCategoryAndVerifyAbsence(ProductCategory productCategory) {
        refreshSession();

        session.remove(productCategory);
        refreshSession();

        ProductCategory deletedProductCategory = session.get(ProductCategory.class, productCategory.getId());
        assertNull(deletedProductCategory);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
