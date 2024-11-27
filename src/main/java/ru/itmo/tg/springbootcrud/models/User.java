package ru.itmo.tg.springbootcrud.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.models.enums.Role;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    @NotNull
    @Size(min = 3, max = 64)
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull
    @Size(min = 8)
    private String password;

    @Column(name = "role", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

}
