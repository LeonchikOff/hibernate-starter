package org.example;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.example.entity.BirthDate;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.entity.orm.type.MyCustomJsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
//        configuration.addAnnotatedClass(User.class);
//        configuration.addAttributeConverter(new BirthDateConverter(), true);
        configuration.registerTypeOverride(new MyCustomJsonBinaryType());
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure("hibernate.cfg.xml");

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .userName("leonchik@gmail.com")
                    .firstName("Leon")
                    .lastName("Chik")
                    .birthDate(new BirthDate(LocalDate.of(2003, 2, 4)))
                    .role(Role.ADMIN)
                    .info("{\"username\" : \"Leon\", " +
                          "\"id\" : \"leon@gmail.com\"}")
                    .build();
            session.persist(user);

            session.getTransaction().commit();
        }
    }
}
