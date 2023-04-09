package org.example;

import org.example.entity.User;
import org.example.jdbc.util.ConnectionManager;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    @Test
    void checkReflectionApi() {
        User user = User.builder()
                .userName("someUserName@gmail.com")
                .firstName("SomeFirstName")
                .lastName("SomeLastName")
                .birthDate(LocalDate.of(1997, 2, 4))
                .age(26)
                .build();

        insertQuery(user);
    }

    private <T> void insertQuery(T obj) {
        String dmlSqlQuery = "insert into %s (%s) values (%s)";
        Class<?> userClass = obj.getClass();
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
                preparedStatement.setObject(i + 1, declaredField.get(obj));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException sqlException) {
            sqlException.printStackTrace();
        }
    }

}