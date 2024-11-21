package itmo.tg.spring_boot_crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1)
    private String name;

    private int lectureHours;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
