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
public class LabWorkResponseDTO {

    private Long id;

    private String name;

    private Coordinates coordinates;

    private Date creationDate;

    private String description;

    private DisciplineResponseDTO discipline;

    private Difficulty difficulty;

    private Integer minimalPoint;

    private Float averagePoint;

    private PersonResponseDTO author;

    private String ownerUsername;

}
