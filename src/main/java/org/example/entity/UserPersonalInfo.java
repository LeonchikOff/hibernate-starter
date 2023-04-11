package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.orm.converter.BirthDateConverter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class UserPersonalInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -7281693586231711545L;

    private String firstName;
    private String lastName;
    @Convert(converter = BirthDateConverter.class)
    private BirthDate birthDate;
}
