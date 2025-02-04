package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.*;
import ru.itmo.tg.springbootcrud.labwork.model.*;
import ru.itmo.tg.springbootcrud.labwork.repository.CoordinatesRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.DisciplineRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.LocationRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.PersonRepository;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelDTOConverter {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final DisciplineRepository disciplineRepository;

    public CoordinatesDTO convert(Coordinates coordinates) {
        return CoordinatesDTO.builder()
                .id(coordinates.getId())
                .x(coordinates.getX())
                .y(coordinates.getY())
                .build();
    }

    public DisciplineDTO convert(Discipline discipline) {
        return DisciplineDTO.builder()
                .id(discipline.getId())
                .name(discipline.getName())
                .lectureHours(discipline.getLectureHours())
                .ownerUsername(discipline.getOwner().getUsername())
                .build();
    }

    public LocationDTO convert(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .x(location.getX())
                .y(location.getY())
                .z(location.getZ())
                .build();
    }

    public PersonDTO convert(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .eyeColor(person.getEyeColor())
                .hairColor(person.getHairColor())
                .locationId(person.getLocation().getId())
                .passportId(person.getPassportID())
                .nationality(person.getNationality())
                .ownerUsername(person.getOwner().getUsername())
                .build();
    }

    public LabWorkDTO convert(LabWork labWork) {
        return LabWorkDTO.builder()
                .id(labWork.getId())
                .name(labWork.getName())
                .coordinatesId(labWork.getCoordinates().getId())
                .description(labWork.getDescription())
                .disciplineId(labWork.getDiscipline().getId())
                .difficulty(labWork.getDifficulty())
                .minimalPoint(labWork.getMinimalPoint())
                .averagePoint(labWork.getAveragePoint())
                .authorId(labWork.getAuthor().getId())
                .ownerUsername(labWork.getOwner().getUsername())
                .build();
    }

    public Coordinates convert(CoordinatesDTO coordinatesDTO) {
        return Coordinates.builder()
                .id(coordinatesDTO.getId())
                .x(coordinatesDTO.getX())
                .y(coordinatesDTO.getY())
                .build();
    }

    public Discipline convert(DisciplineDTO disciplineDTO) {
        return Discipline.builder()
                .id(disciplineDTO.getId())
                .name(disciplineDTO.getName())
                .lectureHours(disciplineDTO.getLectureHours())
                .owner(userRepository.findByUsername(disciplineDTO.getOwnerUsername()).orElseThrow())
                .build();
    }

    public Location convert(LocationDTO locationDTO) {
        return Location.builder()
                .id(locationDTO.getId())
                .x(locationDTO.getX())
                .y(locationDTO.getY())
                .z(locationDTO.getZ())
                .owner(userRepository.findByUsername(locationDTO.getOwnerUsername()).orElseThrow())
                .build();
    }

    public Person convert(PersonDTO personDTO) {
        return Person.builder()
                .id(personDTO.getId())
                .eyeColor(personDTO.getEyeColor())
                .hairColor(personDTO.getHairColor())
                .location(locationRepository.findById(personDTO.getLocationId()).orElseThrow())
                .passportID(personDTO.getPassportId())
                .nationality(personDTO.getNationality())
                .owner(userRepository.findByUsername(personDTO.getOwnerUsername()).orElseThrow())
                .build();
    }

    public LabWork convert(LabWorkDTO labWorkDTO) {
        return LabWork.builder()
                .id(labWorkDTO.getId())
                .name(labWorkDTO.getName())
                .coordinates(coordinatesRepository.findById(labWorkDTO.getCoordinatesId()).orElseThrow())
                .description(labWorkDTO.getDescription())
                .discipline(disciplineRepository.findById(labWorkDTO.getDisciplineId()).orElseThrow())
                .difficulty(labWorkDTO.getDifficulty())
                .minimalPoint(labWorkDTO.getMinimalPoint())
                .averagePoint(labWorkDTO.getAveragePoint())
                .author(personRepository.findById(labWorkDTO.getAuthorId()).orElseThrow())
                .owner(userRepository.findByUsername(labWorkDTO.getOwnerUsername()).orElseThrow())
                .build();
    }

    public List<LabWorkDTO> toLabWorkDTOList(List<LabWork> labWorkList) {
        return labWorkList.stream().map(this::convert).toList();
    }

    public List<LabWork> toLabWorkList(List<LabWorkDTO> labWorkDTOList) {
        return labWorkDTOList.stream().map(this::convert).toList();
    }

}
