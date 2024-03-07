package org.hw.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartItemCrudIManagementT extends HibernateTestBase {

    private final int quantityForProduct1 = 2;
    private final int quantityForProduct2 = 3;
    private Product product1;
    private Product product2;

    private User user;

    @BeforeEach
    public void setUp() {
        super.setUp();
        product1 = testEntityGenerator.createAndPersistProduct("Espresso", "ESP230", "Short description of Espresso",
                "Full description of Espresso", new BigDecimal("2.99"), "http://images.com/espresso.jpg",
                100, LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        product2 = testEntityGenerator.createAndPersistProduct("Tea", "TEA456", "Short description of tea",
                "Full description of tea", new BigDecimal("1.75"), "http://images.com/tea.jpg",
                100, LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        user = testEntityGenerator.createAndPersistUser("Eva", "Dorn", "eva.dorn@cartitem-test.com");
    }

    @Test
    void shouldCreateAndReadCartItems() {
        CartItem cartItem1 = testEntityGenerator.createAndPersistCartItem(user, product1, quantityForProduct1);
        CartItem cartItem2 = testEntityGenerator.createAndPersistCartItem(user, product2, quantityForProduct2);
        int expectedTotalItems = quantityForProduct1 + quantityForProduct2;
        verifyCartItemIsPersisted(user, expectedTotalItems);
    }

    @Test
    void shouldUpdateCartItem() {
        CartItem newCartItem = testEntityGenerator.createAndPersistCartItem(user, product1, quantityForProduct1);
        changeCartItemQuantityAndVerify(newCartItem, 25);
    }

    @Test
    void shouldDeleteSingleCartItem() {
        CartItem newCartItem = testEntityGenerator.createAndPersistCartItem(user, product1, quantityForProduct1);
        deleteCartItemAndVerifyAbsence(newCartItem);
    }

    @Test
    void shouldClearUserCart() {
        CartItem cartItem1 = testEntityGenerator.createAndPersistCartItem(user, product1, quantityForProduct1);
        CartItem cartItem2 = testEntityGenerator.createAndPersistCartItem(user, product2, quantityForProduct2);

        clearUserCartAndVerify(user);
    }

    private void verifyCartItemIsPersisted(User user, int expectedTotalItems) {
        refreshSession();

        List<CartItem> cartItems = loadCartItemsForUser(user);
        assertEquals(expectedTotalItems, cartItems.stream().mapToInt(CartItem::getQuantity).sum());
        assertCartContainsProduct(cartItems, product1.getId(), quantityForProduct1);
        assertCartContainsProduct(cartItems, product2.getId(), quantityForProduct2);

        CartItem itemForProduct1 = findCartItemByProductId(cartItems, product1.getId());
        assertNotNull(itemForProduct1);
        assertEquals(quantityForProduct1, itemForProduct1.getQuantity());

        CartItem itemForProduct2 = findCartItemByProductId(cartItems, product2.getId());
        assertNotNull(itemForProduct2);
        assertEquals(quantityForProduct2, itemForProduct2.getQuantity());
    }

    private void changeCartItemQuantityAndVerify(CartItem cartItem, Integer newQuantity) {
        refreshSession();

        cartItem.setQuantity(newQuantity);
        session.merge(cartItem);
        refreshSession();

        CartItem updatedCartItem = session.get(CartItem.class, cartItem.getId());
        assertNotNull(updatedCartItem);
        assertEquals(newQuantity, updatedCartItem.getQuantity());
    }

    private void deleteCartItemAndVerifyAbsence(CartItem cartItem) {
        refreshSession();

        session.remove(cartItem);
        refreshSession();

        CartItem deletedCartItem = session.get(CartItem.class, cartItem.getId());
        assertNull(deletedCartItem);
    }

    private void clearUserCartAndVerify(User user) {
        refreshSession();

        List<CartItem> userCartItems = loadCartItemsForUser(user);

        userCartItems.forEach(session::remove);
        refreshSession();

        List<CartItem> clearedCartItems = loadCartItemsForUser(user);
        assertTrue(clearedCartItems.isEmpty(), "The cart should be empty after clearing");
    }

    private List<CartItem> loadCartItemsForUser(User user) {
        String query = "SELECT ci FROM CartItem ci WHERE ci.user = :user";
        List<CartItem> cartItems = session.createQuery(query, CartItem.class)
                .setParameter("user", user)
                .getResultList();
        return cartItems;
    }

    private void assertCartContainsProduct(List<CartItem> cartItems, Long productId, int expectedQuantity) {
        CartItem item = findCartItemByProductId(cartItems, productId);
        assertNotNull(item, "Product with ID " + productId + " not found in cart");
        assertEquals(expectedQuantity, item.getQuantity());
    }

    private CartItem findCartItemByProductId(List<CartItem> cartItems, Long productId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}

