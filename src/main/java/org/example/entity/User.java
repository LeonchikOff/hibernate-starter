package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @EmbeddedId
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private UserPersonalInfo personalInfo;

    @Column(unique = true)
    private String userName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Type(type = "myJsonB")
    private String info;
}
