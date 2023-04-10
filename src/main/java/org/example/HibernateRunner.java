package org.example;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.orm.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateRunner.class);
    public static void main(String[] args) {

//      The entity status is TRANSIENT(переходный) for whatever session
        User user = User.builder()
                .userName("someUserName@gmile.com")
                .firstName("someFirstName")
                .lastName("someLastName")
                .build();
        LOGGER.info("User entity status is TRANSIENT for whatever session: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction1 = session.beginTransaction();
                LOGGER.info("Transaction is created: {}", transaction1);
//      The entity status is PERSISTENT(стабильный) for the current session
                session.saveOrUpdate(user);
                LOGGER.info("User entity: {} has PERSISTENT status for session: {}", user, session);
                transaction1.commit();
            }
            LOGGER.warn("User entity has DETACHED status for closed session: {}", session);
        } catch (Exception e) {
            LOGGER.error("Exception occurred", e);
            throw e;
        }

    }
}
