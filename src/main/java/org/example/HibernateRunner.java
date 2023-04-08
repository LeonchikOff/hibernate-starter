package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration hiberConfiguration = new Configuration();
        hiberConfiguration.configure("hibernate.cfg.xml");
        try (SessionFactory sessionFactory = hiberConfiguration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            System.out.println(session);
        }
    }
}
