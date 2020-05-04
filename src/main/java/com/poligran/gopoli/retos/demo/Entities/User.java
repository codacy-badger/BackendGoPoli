package com.poligran.gopoli.retos.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String firts_name;

    @NotBlank
    @Size(max = 40)
    private String last_name;

    @NotBlank
    @Size(max = 20)
    private String phone_number;

    @ManyToOne
    @JoinColumn (name = "role_id")
    private Type_User typeUser;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User(String firts_name, String last_name, String phone_number, Type_User typeUser, String username, String email, String password) {
        this.firts_name = firts_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.typeUser = typeUser;
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
