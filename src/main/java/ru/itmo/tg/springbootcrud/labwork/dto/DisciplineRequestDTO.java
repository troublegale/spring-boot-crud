package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplineRequestDTO {

    private String name;

    private Integer lectureHours;

}
