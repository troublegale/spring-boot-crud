package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.Coordinates;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabWorkRequestDTO {

    private String name;

    private Coordinates coordinates;

    private String description;

    private DisciplineRequestDTO discipline;

    private Difficulty difficulty;

    private Integer minimalPoint;

    private Float averagePoint;

    private PersonRequestDTO author;

}
