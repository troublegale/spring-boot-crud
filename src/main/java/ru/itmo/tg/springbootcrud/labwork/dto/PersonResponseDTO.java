package ru.itmo.tg.springbootcrud.labwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.labwork.model.Location;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Color;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Country;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonResponseDTO {

    private Long id;

    private String name;

    private Color eyeColor;

    private Color hairColor;

    private Location location;

    private String passportId;

    private Country nationality;

    private String ownerUsername;

}
