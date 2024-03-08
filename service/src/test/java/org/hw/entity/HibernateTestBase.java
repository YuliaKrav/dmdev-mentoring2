package org.hw.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hw.util.HibernateTestUtil;
import org.hw.util.TestEntityGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class HibernateTestBase {

    protected SessionFactory sessionFactory;
    protected Session session;
    protected Transaction transaction;
    protected TestEntityGenerator testEntityGenerator;

    @BeforeAll
    void setUpClass() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        testEntityGenerator = new TestEntityGenerator(session);
    }

    @AfterEach
    void tearDown() {
        if (transaction.isActive()) {
            transaction.rollback();
        }
        if (session.isOpen()) {
            session.close();
        }
    }

    @AfterAll
    void tearDownClass() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
