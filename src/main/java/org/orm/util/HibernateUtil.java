package org.orm.util;

import com.vladmihalcea.hibernate.type.util.CamelCaseToSnakeCaseNamingStrategy;
import lombok.experimental.UtilityClass;
import org.example.entity.User;
import org.example.entity.orm.converter.BirthDateConverter;
import org.example.entity.orm.type.MyCustomJsonBinaryType;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAttributeConverter(BirthDateConverter.INSTANCE);
        configuration.registerTypeOverride(MyCustomJsonBinaryType.INSTANCE);
        configuration.setPhysicalNamingStrategy(CamelCaseToSnakeCaseNamingStrategy.INSTANCE);
        configuration.configure("hibernate.cfg.xml");
        return configuration.buildSessionFactory();
    }
}
