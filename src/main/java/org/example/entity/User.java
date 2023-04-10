package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.orm.converter.BirthDateConverter;
import org.example.entity.orm.type.MyCustomJsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public")
@TypeDefs({
        @TypeDef(name = "myJsonB", typeClass = MyCustomJsonBinaryType.class)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_name")
    private String userName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birth_date")
    @Convert(converter = BirthDateConverter.class)
    private BirthDate birthDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Type(type = "myJsonB")
    private String info;
}
