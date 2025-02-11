package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.*;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;
import ru.itmo.tg.springbootcrud.labwork.model.Person;
import ru.itmo.tg.springbootcrud.labwork.model.UpdateHistory;
import ru.itmo.tg.springbootcrud.security.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelDTOConverter {

    public DisciplineResponseDTO convert(Discipline discipline) {
        return DisciplineResponseDTO.builder()
                .id(discipline.getId())
                .name(discipline.getName())
                .lectureHours(discipline.getLectureHours())
                .ownerUsername(discipline.getOwner().getUsername())
                .build();
    }

    public Discipline convert(DisciplineRequestDTO dto, User owner) {
        return Discipline.builder()
                .name(dto.getName())
                .lectureHours(dto.getLectureHours())
                .owner(owner)
                .build();
    }

    public List<DisciplineResponseDTO> toDisciplineResponseDTOList(List<Discipline> disciplineList) {
        return disciplineList.stream().map(this::convert).toList();
    }

    public List<Discipline> toDisciplineList(List<DisciplineRequestDTO> dtoList, User owner) {
        return dtoList.stream().map(dto -> convert(dto, owner)).toList();
    }

    public PersonResponseDTO convert(Person person) {
        return PersonResponseDTO.builder()
                .id(person.getId())
                .name(person.getName())
                .eyeColor(person.getEyeColor())
                .hairColor(person.getHairColor())
                .location(person.getLocation())
                .passportId(person.getPassportID())
                .nationality(person.getNationality())
                .ownerUsername(person.getOwner().getUsername())
                .build();
    }

    public Person convert(PersonRequestDTO dto, User owner) {
        return Person.builder()
                .name(dto.getName())
                .eyeColor(dto.getEyeColor())
                .hairColor(dto.getHairColor())
                .location(dto.getLocation())
                .passportID(dto.getPassportId())
                .nationality(dto.getNationality())
                .owner(owner)
                .build();
    }

    public List<PersonResponseDTO> toPersonResponseDTOList(List<Person> personList) {
        return personList.stream().map(this::convert).toList();
    }

    public List<Person> toPersonList(List<PersonRequestDTO> dtoList, User owner) {
        return dtoList.stream().map(dto -> convert(dto, owner)).toList();
    }

    public LabWorkResponseDTO convert(LabWork labWork) {
        return LabWorkResponseDTO.builder()
                .id(labWork.getId())
                .name(labWork.getName())
                .coordinates(labWork.getCoordinates())
                .creationDate(labWork.getCreationDate())
                .description(labWork.getDescription())
                .discipline(convert(labWork.getDiscipline()))
                .difficulty(labWork.getDifficulty())
                .minimalPoint(labWork.getMinimalPoint())
                .averagePoint(labWork.getAveragePoint())
                .author(convert(labWork.getAuthor()))
                .ownerUsername(labWork.getOwner().getUsername())
                .build();
    }

    public LabWork convert(LabWorkRequestDTO dto, User owner) {
        return LabWork.builder()
                .name(dto.getName())
                .coordinates(dto.getCoordinates())
                .description(dto.getDescription())
                .discipline(dto.getDiscipline() == null ? null : convert(dto.getDiscipline(), owner))
                .difficulty(dto.getDifficulty())
                .minimalPoint(dto.getMinimalPoint())
                .averagePoint(dto.getAveragePoint())
                .author(dto.getAuthor() == null ? null : convert(dto.getAuthor(), owner))
                .owner(owner)
                .build();
    }

    public List<LabWorkResponseDTO> toLabWorkResponseDTOList(List<LabWork> labWorkList) {
        return labWorkList.stream().map(this::convert).toList();
    }

    public List<LabWork> toLabWorkList(List<LabWorkRequestDTO> dtoList, User owner) {
        return dtoList.stream().map(dto -> convert(dto, owner)).toList();
    }

    public UpdateHistoryDTO convert(UpdateHistory updateHistory) {
        return UpdateHistoryDTO.builder()
                .labWorkId(updateHistory.getLabWorkID())
                .username(updateHistory.getUser().getUsername())
                .action(updateHistory.getAction())
                .actionTime(updateHistory.getActionTime())
                .build();
    }

    public List<UpdateHistoryDTO> toUpdateHistoryDTOList(List<UpdateHistory> updateHistoryList) {
        return updateHistoryList.stream().map(this::convert).toList();
    }

}
