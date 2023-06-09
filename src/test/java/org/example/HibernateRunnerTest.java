package org.example;

import lombok.Cleanup;
import org.example.entity.*;
import org.example.jdbc.util.ConnectionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.orm.util.HibernateUtil;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    void checkOneToOne() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()){
            session.beginTransaction();
            User user = User.builder()
                    .userName("someUserName@gmail.com")
                    .personalInfo(UserPersonalInfo.builder()
                            .firstName("firstName")
                            .lastName("lastName")
                            .birthDate(BirthDate.of(LocalDate.of(1993, 6, 20)))
                            .build())
                    .role(Role.ADMIN)
                    .build();
            Profile profile = Profile.builder()
                    .language("En")
                    .street("Bad 13")
                    .build();
            profile.setUser(user);
            session.save(user);


            session.getTransaction().commit();
        }
    }

    @Test
    @Disabled
    void checkOrphanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Company company = session.get(Company.class, 1);
            Set<User> users = company.getUsers();
            users.removeIf(user -> user.getId().equals(6L));

            session.getTransaction().commit();
        }
    }

    @Test
    @Disabled("LazyException")
    void checkLazyInitialisation() {
        Company company = null;
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                company = session.get(Company.class, 1);


                session.getTransaction().commit();
            }
        }
        System.out.println(company.getUsers().size());
    }


    @Test
    @Disabled
    void deleteCompany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Company company = session.get(Company.class, 6);
                session.delete(company);
                session.getTransaction().commit();
            }
        }
    }

    @Disabled
    @Test
    void addUserToNewCompany() {
        @Cleanup
        SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();
        Company facebook = Company.builder()
                .name("Facebook")
                .build();
        User user = User.builder()
                .userName("someUserName@gmail.com")
                .personalInfo(UserPersonalInfo.builder()
                        .firstName("firstName")
                        .lastName("lastName")
                        .birthDate(BirthDate.of(LocalDate.of(1993, 6, 20)))
                        .build())
                .role(Role.ADMIN)
                .build();
        facebook.addUser(user);
        session.save(facebook);
        session.getTransaction().commit();
    }

    @Disabled
    @Test
    void companyAssociationOneToMany() {
        @Cleanup
        SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);
        System.out.println("");
        session.getTransaction().commit();

    }


    @Test
    @Disabled("non-working mapping, illustrating the general idea of the Hibernate's Session method get(class, id)")
    void checkSelectReflectionApi() {
        User user = selectQuery(User.class, "someUserName@gmail.com");

    }

    @SuppressWarnings("SameParameterValue")
    private <T> T selectQuery(Class<T> entityType, String id) {
        String dmlSelectSqlQuery = "select * from %s where %s = ?";
        String tableName = Optional.ofNullable(entityType.getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(entityType.getName());
        Field[] declaredFields = entityType.getDeclaredFields();
        String columnNameId = null;
        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Id.class) != null) {
                columnNameId = Optional.ofNullable(declaredField.getAnnotation(Column.class))
                        .map(Column::name).orElse(declaredField.getName());
            }
        }
        dmlSelectSqlQuery = String.format(dmlSelectSqlQuery, tableName, columnNameId);
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(dmlSelectSqlQuery)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                T instance = entityType.getConstructor().newInstance();
                populateEntity(entityType, declaredFields, resultSet, instance);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    private <T> void populateEntity(Class<T> entityType, Field[] declaredFields, ResultSet resultSet, T instance) throws IllegalAccessException, SQLException {
        Field[] declaredFields1 = entityType.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields1[i];
            field.setAccessible(true);
            field.set(instance, resultSet.getObject(1));
        }
    }

    @Test
    @Disabled
    void checkInsertReflectionApi() {
//        User user = User.builder()
//                .userName("someUserName@gmail.com")
//                .firstName("SomeFirstName")
//                .lastName("SomeLastName")
//                .birthDate(new BirthDate(LocalDate.of(1997, 2, 4)))
//                .role(Role.USER)
//                .build();
//        insertQuery(user);
    }

    private <T> void insertQuery(T entity) {
        String dmlSqlQuery = "insert into %s (%s) values (%s)";
        Class<?> userClass = entity.getClass();
        String tableName = Optional.ofNullable(userClass.getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(userClass.getName());
        Field[] declaredFields = userClass.getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field ->
                        Optional.ofNullable(field.getAnnotation(Column.class))
                                .map(Column::name)
                                .orElse(field.getName()))
                .collect(Collectors.joining(", "));
        String prepareColumnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        String insertQueryDml = String.format(dmlSqlQuery, tableName, columnNames, prepareColumnValues);
        System.out.println(insertQueryDml);

        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQueryDml)) {
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                Object fieldOfEntityValue = declaredField.get(entity);
                if (declaredField.getType().isEnum()) {
                    EnumType enumType = Optional.ofNullable(declaredField.getAnnotation(Enumerated.class))
                            .map(Enumerated::value).orElse(EnumType.ORDINAL);
                    var anEnum = declaredField.getType().asSubclass(Enum.class).cast(fieldOfEntityValue);
                    preparedStatement.setObject(i + 1, enumType.equals(EnumType.ORDINAL) ? anEnum.ordinal() : anEnum.name());
                    continue;
                }
                preparedStatement.setObject(i + 1, fieldOfEntityValue);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException sqlException) {
            sqlException.printStackTrace();
        }
    }

}