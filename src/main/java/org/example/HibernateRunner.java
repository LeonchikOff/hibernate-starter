package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.BirthDate;
import org.example.entity.User;
import org.example.entity.UserPersonalInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.orm.util.HibernateUtil;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {
    public static void main(String[] args) {

//      The entity status is TRANSIENT(переходный) for whatever session
        User user = User.builder()
                .userName("anotherUserName@gmile.com")
                .personalInfo(UserPersonalInfo.builder()
                        .firstName("anotherFirstName")
                        .lastName("anotherLastName")
                        .birthDate(BirthDate.of(LocalDate.of(1993, 6, 20)))
                        .build())
                .build();
        log.info("User entity status is TRANSIENT for whatever session: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction1 = session.beginTransaction();
                log.info("Transaction is created: {}", transaction1);
//      The entity status is PERSISTENT(стабильный) for the current session
                session.saveOrUpdate(user);
                log.info("User entity: {} has PERSISTENT status for session: {}", user, session);
                transaction1.commit();
            }
            log.warn("User entity has DETACHED status for closed session: {}", session);
        } catch (Exception e) {
            log.error("Exception occurred", e);
            throw e;
        }

    }
}
