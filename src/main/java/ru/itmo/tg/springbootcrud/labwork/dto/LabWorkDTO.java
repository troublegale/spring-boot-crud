package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private DisciplineDTO discipline;

    private Integer minimalPoint;

    private Float averagePoint;

    private PersonDTO personDTO;

    private Long userId;

}
