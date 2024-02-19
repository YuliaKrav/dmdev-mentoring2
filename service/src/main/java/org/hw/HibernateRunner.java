package org.hw;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hw.util.HibernateUtil;

public class HibernateRunner {

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            System.out.println("Started...");

            session.beginTransaction();

            session.getTransaction().commit();
        }
    }
}
