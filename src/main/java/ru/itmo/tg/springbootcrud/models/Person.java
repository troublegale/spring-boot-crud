package ru.itmo.tg.springbootcrud.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.models.enums.Color;
import ru.itmo.tg.springbootcrud.models.enums.Country;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User owner;

}
