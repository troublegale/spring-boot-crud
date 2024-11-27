package ru.itmo.tg.springbootcrud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disciplines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    @NotNull
    @NotBlank
    private String name;

    @Column(name = "lecture_hours")
    private int lectureHours;

}
