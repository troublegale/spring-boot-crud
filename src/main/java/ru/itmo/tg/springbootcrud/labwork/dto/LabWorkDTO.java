package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabWorkDTO {

    private Long id;

    private String name;

    private Double x;

    private Long y;

    private String description;

    private Difficulty difficulty;

    private DisciplineDTO disciplineDTO;

    private Integer minimalPoint;

    private Float averagePoint;

    private PersonDTO personDTO;

    private String ownerUsername;

}
