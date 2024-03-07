package org.hw.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil {

    private static final PostgreSQLContainer<?> postgresdb = new PostgreSQLContainer<>("postgres:14");

    static {
        postgresdb.start();
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = HibernateUtil.buildConfiguration();
        configuration.setProperty("hibernate.connection.url", postgresdb.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgresdb.getUsername());
        configuration.setProperty("hibernate.connection.password", postgresdb.getPassword());
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
