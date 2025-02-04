package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabWorkDTO {

    private Long id;

    private String name;

    private long coordinatesId;

    private String description;

    private long disciplineId;

    private Difficulty difficulty;

    private int minimalPoint;

    private float averagePoint;

    private long authorId;

    private String ownerUsername;

}
