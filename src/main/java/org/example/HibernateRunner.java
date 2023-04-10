package org.example;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.orm.util.HibernateUtil;

public class HibernateRunner {
    public static void main(String[] args) {

//      The entity status is TRANSIENT(переходный) for whatever session
        User user = User.builder()
                .userName("someUserName@gmile.com")
                .firstName("someFirstName")
                .lastName("someLastName")
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                Transaction transaction1 = session1.beginTransaction();
//      The entity status is PERSISTENT(стабильный) for the current session
                session1.saveOrUpdate(user);
                transaction1.commit();
            }
            try (Session session3 = sessionFactory.openSession()) {
                Transaction transaction2 = session3.beginTransaction();
                session3.refresh(user);
                transaction2.commit();
            }

            try (Session session2 = sessionFactory.openSession()) {
                Transaction transaction2 = session2.beginTransaction();
//      The entity status is REMOVED(удалённый) for the current session
                session2.delete(user);
                transaction2.commit();
            }
        }

    }
}
