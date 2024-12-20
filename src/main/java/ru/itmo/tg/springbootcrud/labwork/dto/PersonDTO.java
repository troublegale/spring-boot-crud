package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Color;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Country;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {

    private Long id;

    private Color eyeColor;

    private Color hairColor;

    private Long x;

    private Double y;

    private Float z;

    private String passportID;

    private Country nationality;

    private String ownerUsername;

}
