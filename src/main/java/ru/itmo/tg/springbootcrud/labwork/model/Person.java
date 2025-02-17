package ru.itmo.tg.springbootcrud.labwork.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Color;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Country;
import ru.itmo.tg.springbootcrud.security.model.User;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull
    @NotBlank
    private String name;

    @Column(name = "eye_color", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Color eyeColor;

    @Column(name = "hair_color")
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @JoinColumn(name = "location_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    @Column(name = "passport_id", unique = true)
    @NotBlank
    @Size(max = 40)
    private String passportID;

    @Column(name = "nationality", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Country nationality;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    @NotNull
    private User owner;

}
