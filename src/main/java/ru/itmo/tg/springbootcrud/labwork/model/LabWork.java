package ru.itmo.tg.springbootcrud.labwork.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;
import ru.itmo.tg.springbootcrud.security.model.User;

import java.util.Date;

@Entity
@Table(name = "lab_works")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull
    @NotBlank
    private String name;

    @JoinColumn(name = "coordinates_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Builder.Default
    private Date creationDate = new Date();

    @Column(name = "description")
    @NotBlank
    private String description;

    @JoinColumn(name = "discipline_id", nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @NotNull
    private Discipline discipline;

    @Column(name = "difficulty")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "minimal_point")
    @Positive
    private int minimalPoint;

    @Column(name = "average_point")
    @Positive
    private float averagePoint;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @NotNull
    private Person author;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    @NotNull
    private User owner;

}
