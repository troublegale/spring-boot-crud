package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplineResponseDTO {

    private Long id;

    private String name;

    private Integer lectureHours;

    private String ownerUsername;

}
