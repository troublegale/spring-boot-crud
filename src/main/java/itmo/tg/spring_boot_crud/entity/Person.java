package itmo.tg.spring_boot_crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    private Color eyeColor;

    private Color hairColor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDateTime birthday;

    @Min(value = 1)
    private double height;

    @NotNull
    @Min(value = 1)
    private double weight;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
