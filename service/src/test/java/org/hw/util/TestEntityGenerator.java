package org.hw.util;

import org.hibernate.Session;
import org.hw.entity.Address;
import org.hw.entity.Brand;
import org.hw.entity.CartItem;
import org.hw.entity.City;
import org.hw.entity.Country;
import org.hw.entity.Currency;
import org.hw.entity.Order;
import org.hw.entity.OrderItem;
import org.hw.entity.OrderStatus;
import org.hw.entity.Product;
import org.hw.entity.ProductAnalog;
import org.hw.entity.ProductCategory;
import org.hw.entity.Review;
import org.hw.entity.Role;
import org.hw.entity.RoleName;
import org.hw.entity.StateProvince;
import org.hw.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestEntityGenerator {

    private final Session session;

    public TestEntityGenerator(Session session) {
        this.session = session;
    }

    public User createAndPersistUser(String firstName, String lastName, String email) {
        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();

        session.persist(newUser);
        return newUser;
    }

    public User createAndPersistUser(String firstName, String lastName, String email, String password, boolean enabled) {
        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .enabled(enabled).build();

        session.persist(newUser);
        return newUser;
    }

    public Role createAndPersistRole(RoleName roleName) {
        Role newRole = Role.builder()
                .name(roleName)
                .build();

        session.persist(newRole);
        return newRole;
    }

    public Country createAndPersistCountry(String name, String code) {
        Country newCountry = Country.builder()
                .name(name)
                .code(code)
                .build();

        session.persist(newCountry);
        return newCountry;
    }

    public StateProvince createAndPersistStateProvince(String name) {
        StateProvince newStateProvince = StateProvince.builder()
                .name(name)
                .build();

        session.persist(newStateProvince);
        return newStateProvince;
    }

    public StateProvince createAndPersistStateProvince(String name, Country country) {
        StateProvince newStateProvince = StateProvince.builder()
                .name(name)
                .country(country)
                .build();

        session.persist(newStateProvince);
        return newStateProvince;
    }

    public City createAndPersistCity(String name) {
        City newCity = City.builder()
                .name(name)
                .build();

        session.persist(newCity);
        return newCity;
    }

    public City createAndPersistCity(String name, StateProvince stateProvince) {
        City newCity = City.builder()
                .name(name)
                .stateProvince(stateProvince)
                .build();

        session.persist(newCity);
        return newCity;
    }

    public Address createAndPersistAddress(String street, String zipCode, City city, StateProvince stateProvince, Country country) {
        Address newAddress = Address.builder()
                .street(street)
                .zipCode(zipCode)
                .city(city)
                .stateProvince(stateProvince)
                .country(country)
                .build();

        session.persist(newAddress);
        return newAddress;
    }

    public Brand createAndPersistBrand(String name, String logo) {
        Brand newBrand = Brand.builder()
                .name(name)
                .logo(logo)
                .build();

        session.persist(newBrand);
        return newBrand;
    }

    public ProductCategory createAndPersistProductCategory(String name) {
        ProductCategory newProductCategory = ProductCategory.builder()
                .categoryName(name)
                .build();

        session.persist(newProductCategory);
        return newProductCategory;
    }

    public Product createAndPersistProduct(String name, String sku, String descriptionShort,
                                           String descriptionFull, BigDecimal unitPrice, String imageUrl,
                                           Integer unitsInStock, LocalDateTime dateCreated, LocalDateTime lastUpdated,
                                           ProductCategory category, Brand brand, Country countryOfOrigin) {
        Product product = Product.builder()
                .name(name)
                .sku(sku)
                .descriptionShort(descriptionShort)
                .descriptionFull(descriptionFull)
                .unitPrice(unitPrice)
                .imageUrl(imageUrl)
                .unitsInStock(unitsInStock)
                .dateCreated(dateCreated)
                .lastUpdated(lastUpdated)
                .category(category)
                .brand(brand)
                .countryOfOrigin(countryOfOrigin)
                .build();

        session.persist(product);
        return product;
    }

    public ProductAnalog createAndPersistProductAnalog(Product product, Product analogProduct) {
        ProductAnalog newProductAnalog = ProductAnalog.builder()
                .product(product)
                .analogProduct(analogProduct)
                .build();

        session.persist(newProductAnalog);
        return newProductAnalog;
    }

    public Currency createAndPersistCurrency(String name, String symbol) {
        Currency newCurrency = Currency.builder()
                .name(name)
                .symbol(symbol)
                .build();
        session.persist(newCurrency);
        return newCurrency;
    }

    public Order createAndPersistOrder(String orderTrackingNumber, BigDecimal totalPrice, Integer totalQuantity,
                                       OrderStatus status, User customer, Address shippingAddress,
                                       Address billingAddress, Currency currency) {
        Order newOrder = Order.builder()
                .orderTrackingNumber(orderTrackingNumber)
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .status(status)
                .dateCreated(LocalDateTime.now())
                .lastUpdated(LocalDateTime.now())
                .customer(customer)
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .currency(currency)
                .build();

        session.persist(newOrder);
        return newOrder;
    }

    public OrderItem createAndPersistOrderItem(Order order, Product product, int quantity, BigDecimal unitPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(unitPrice);

        session.persist(orderItem);
        return orderItem;
    }

    public Review createAndPersistReview(String headline, String comment, Integer rating, LocalDateTime dateCreated, Product product, User user) {
        Review review = Review.builder()
                .headline(headline)
                .comment(comment)
                .rating(rating)
                .dateCreated(dateCreated)
                .product(product)
                .user(user)
                .build();

        session.persist(review);
        return review;
    }

    public CartItem createAndPersistCartItem(User user, Product product, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        session.persist(cartItem);
        return cartItem;
    }
}