package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.orm.util.HibernateUtil;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {
    public static void main(String[] args) {
        Company companyGoogle = Company.builder()
                .name("Amazon")
                .build();
        User user = User.builder()
                .userName("userName@gmile.com")
                .personalInfo(UserPersonalInfo.builder()
                        .firstName("firstName")
                        .lastName("lastName")
                        .birthDate(BirthDate.of(LocalDate.of(1993, 6, 20)))
                        .build())
                .role(Role.ADMIN)
                .company(companyGoogle)
                .build();

        User userFromDb = null;
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();

                session.save(companyGoogle);
                session.save(user);
                transaction.commit();
            }
        }



    }
}
