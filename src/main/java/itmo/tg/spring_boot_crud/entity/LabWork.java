package itmo.tg.spring_boot_crud.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "lab_work")
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    private ZonedDateTime creationDate;

    private String description;

    private Difficulty difficulty;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    private double minimal_point;

    private double average_point;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id", nullable = false)
    private Person author;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
