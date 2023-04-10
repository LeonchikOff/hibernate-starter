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
    @Id
//    @GeneratedValue(generator = "users_id_gen", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "users_id_table_gen")
    @TableGenerator(name = "users_id_table_gen", table = "all_sequences_table",
            pkColumnName = "table_name", valueColumnName = "pk_value",
            initialValue = 0, allocationSize = 1)
    private Long id;
    @Column(unique = true)
    private String userName;
    @Embedded
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private UserPersonalInfo personalInfo;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Type(type = "myJsonB")
    private String info;
}
