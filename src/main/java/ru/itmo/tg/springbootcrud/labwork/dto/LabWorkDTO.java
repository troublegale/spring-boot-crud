package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.Coordinates;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabWorkDTO {

    private Long id;

    private String name;

    private Coordinates coordinates;

    private String description;

    private DisciplineDTO discipline;

    private Difficulty difficulty;

    private int minimalPoint;

    private float averagePoint;

    private PersonDTO author;

    private String ownerUsername;

}
