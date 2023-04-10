package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.orm.converter.BirthDateConverter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class UserPersonalInfo {
    private String firstName;
    private String lastName;
    @Convert(converter = BirthDateConverter.class)
    private BirthDate birthDate;
}
