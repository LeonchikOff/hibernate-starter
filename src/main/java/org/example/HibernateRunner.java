package org.example;

import org.example.entity.Role;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration hiberConfiguration = new Configuration();
//        hiberConfiguration.addAnnotatedClass(User.class);
        hiberConfiguration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        hiberConfiguration.configure("hibernate.cfg.xml");

        try (SessionFactory sessionFactory = hiberConfiguration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .userName("leon@gmail.com")
                    .firstName("Leon")
                    .lastName("Chik")
                    .birthDate(LocalDate.of(2003, 2, 4))
                    .age(20)
                    .role(Role.ADMIN)
                    .build();
            session.persist(user);

            session.getTransaction().commit();
        }
    }
}
