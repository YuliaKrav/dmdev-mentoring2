package org.hw.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderCrudManagementIT extends HibernateTestBase {

    private Order newOrder;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        newOrder = createTestOrder();
    }

    @Test
    void shouldCreateAndReadOrder() {
        verifyOrderIsPersisted(newOrder);
    }

    @Test
    void shouldUpdateOrder() {
        changeOrderNameAndVerify(newOrder, OrderStatus.SHIPPED);
    }

    @Test
    void shouldDeleteOrder() {
        deleteOrderAndVerifyAbsence(newOrder);
    }

    private Order createTestOrder() {
        User customer = testEntityGenerator.createAndPersistUser("Eva", "Moon", "eva.moon@test.com");
        Country country = testEntityGenerator.createAndPersistCountry("France", "FR");
        StateProvince stateProvince = testEntityGenerator.createAndPersistStateProvince("Auvergne‐Rhône‐Alpes-create");
        City city = testEntityGenerator.createAndPersistCity("Lyon");
        Address shippingAddress = testEntityGenerator.createAndPersistAddress("Rue de la République", "69002", city, stateProvince, country);
        Address billingAddress = testEntityGenerator.createAndPersistAddress("Place Bellecour", "69002", city, stateProvince, country);
        Currency currency = testEntityGenerator.createAndPersistCurrency("EUR", "€");

        Order newOrder = testEntityGenerator.createAndPersistOrder("Tracking12345", new BigDecimal("1000.00"), 4,
                OrderStatus.PAID, customer, shippingAddress, billingAddress, currency);

        ProductCategory category = testEntityGenerator.createAndPersistProductCategory("Coffee");
        Brand brand = testEntityGenerator.createAndPersistBrand("CafePure", "http://logos.com/cafepure-logo.jpg");
        Country countryProduct = testEntityGenerator.createAndPersistCountry("Brazil", "BR");
        Product groundCoffee = testEntityGenerator.createAndPersistProduct(
                "Ground Coffee", "CP-GC-100", "Ground coffee",
                "CafePure Ground Coffee. Perfect for starting your day.",
                new BigDecimal("5.55"), "http://images.com/ground_coffee.jpg", 200,
                LocalDateTime.now(), LocalDateTime.now(), category, brand, countryProduct
        );

        Product decafCoffee = testEntityGenerator.createAndPersistProduct(
                "Decaf Coffee", "CP-DC-200", "Decaf coffee",
                "CafePure Decaf Coffee. Perfect for enjoying anytime.",
                new BigDecimal("6.66"), "http://images.com/decaf_coffee.jpg", 300,
                LocalDateTime.now(), LocalDateTime.now(), category, brand, countryProduct
        );

        OrderItem coffeeItem = testEntityGenerator.createAndPersistOrderItem(newOrder, groundCoffee, 3, groundCoffee.getUnitPrice());
        OrderItem decafItem = testEntityGenerator.createAndPersistOrderItem(newOrder, decafCoffee, 1, decafCoffee.getUnitPrice());
        newOrder.getOrderItems().add(coffeeItem);
        newOrder.getOrderItems().add(decafItem);

        return newOrder;
    }

    private void verifyOrderIsPersisted(Order order) {
        refreshSession();

        Order retrievedOrder = session.get(Order.class, order.getId());
        assertNotNull(retrievedOrder);
        assertEquals(order.getOrderTrackingNumber(), retrievedOrder.getOrderTrackingNumber());
        assertEquals(order.getTotalPrice(), retrievedOrder.getTotalPrice());
        assertEquals(order.getTotalQuantity(), retrievedOrder.getTotalQuantity());
        assertEquals(order.getStatus(), retrievedOrder.getStatus());

        assertEquals(order.getCustomer().getId(), retrievedOrder.getCustomer().getId());
        assertEquals(order.getShippingAddress().getId(), retrievedOrder.getShippingAddress().getId());
        assertEquals(order.getBillingAddress().getId(), retrievedOrder.getBillingAddress().getId());
        assertEquals(order.getCurrency().getId(), retrievedOrder.getCurrency().getId());

        if (!order.getOrderItems().isEmpty()) {
            assertEquals(order.getOrderItems().size(), retrievedOrder.getOrderItems().size());

            List<Long> orderItemsIds = order.getOrderItems().stream().map(OrderItem::getId).sorted().collect(Collectors.toList());
            List<Long> retrievedOrderItemIds = retrievedOrder.getOrderItems().stream().map(OrderItem::getId).sorted().collect(Collectors.toList());
            assertEquals(orderItemsIds, retrievedOrderItemIds, "OrderItem IDs do not match");

            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItem retrievedItem = retrievedOrder.getOrderItems().stream()
                        .filter(item -> item.getId().equals(orderItem.getId()))
                        .findFirst()
                        .orElseThrow(() -> new AssertionError("OrderItem with ID " + orderItem.getId() + " not found"));

                assertEquals(orderItem.getProduct().getId(), retrievedItem.getProduct().getId(), "Product ID mismatch");
                assertEquals(orderItem.getQuantity(), retrievedItem.getQuantity(), "Quantity mismatch");
                assertEquals(orderItem.getUnitPrice(), retrievedItem.getUnitPrice(), "Unit price mismatch");
            }
        }
    }

    private void changeOrderNameAndVerify(Order order, OrderStatus newOrderStatus) {
        refreshSession();
        order.setStatus(OrderStatus.SHIPPED);
        session.merge(order);
        refreshSession();

        Order updatedOrder = session.get(Order.class, order.getId());
        assertNotNull(updatedOrder);
        assertEquals(newOrderStatus, updatedOrder.getStatus());
    }

    private void deleteOrderAndVerifyAbsence(Order order) {
        refreshSession();

        session.remove(order);
        refreshSession();

        Order deletedOrder = session.get(Order.class, order.getId());
        assertNull(deletedOrder);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}
