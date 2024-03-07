package org.hw.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReviewCrudManagementIT extends HibernateTestBase {

    private Product product;
    private User user;

    @BeforeEach
    public void setUp() {
        super.setUp();
        product = testEntityGenerator.createAndPersistProduct("Espresso", "ESP230", "Short description of Espresso",
                "Full description of Espresso", new BigDecimal("2.99"), "http://images.com/espresso.jpg",
                100, LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        user = testEntityGenerator.createAndPersistUser("Eva", "Dorn", "eva.dorn@review-test.com");
    }

    @Test
    void shouldCreateAndReadReview() {
        Review newReview = testEntityGenerator.createAndPersistReview("Great Coffee!", "I really enjoyed this coffee. Will buy again!", 5, LocalDateTime.now(), product, user);
        verifyReviewIsPersisted(newReview);
    }

    @Test
    void shouldUpdateReview() {
        Review newReview = testEntityGenerator.createAndPersistReview("Average Coffee", "Just okay.", 2, LocalDateTime.now(), product, user);
        changeReviewNameAndVerify(newReview, 3);
    }

    @Test
    void shouldDeleteReview() {
        Review newReview = testEntityGenerator.createAndPersistReview("Good Coffee", "Nice but too strong for me.", 4, LocalDateTime.now(), product, user);
        deleteReviewAndVerifyAbsence(newReview);
    }

    private void verifyReviewIsPersisted(Review review) {
        refreshSession();

        Review retrievedReview = session.get(Review.class, review.getId());
        assertNotNull(retrievedReview);
        assertEquals(review.getHeadline(), retrievedReview.getHeadline());
        assertEquals(review.getComment(), retrievedReview.getComment());
        assertEquals(review.getRating(), retrievedReview.getRating());
        assertEquals(review.getProduct().getId(), retrievedReview.getProduct().getId());
        assertEquals(review.getUser().getId(), retrievedReview.getUser().getId());
    }

    private void changeReviewNameAndVerify(Review review, Integer newRating) {
        refreshSession();

        review.setRating(newRating);
        session.merge(review);
        refreshSession();

        Review updatedReview = session.get(Review.class, review.getId());
        assertNotNull(updatedReview);
        assertEquals(newRating, updatedReview.getRating());
    }

    private void deleteReviewAndVerifyAbsence(Review review) {
        refreshSession();

        session.remove(review);
        refreshSession();

        Review deletedReview = session.get(Review.class, review.getId());
        assertNull(deletedReview);
    }

    private void refreshSession() {
        session.flush();
        session.clear();
    }
}



