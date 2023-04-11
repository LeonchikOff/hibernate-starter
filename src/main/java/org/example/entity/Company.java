package org.example.entity;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "company_id")
    private Set<User> users = new HashSet<>();


    public void addUser(User user) {
        this.users.add(user);
        user.setCompany(this);
    }
}
