package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.UpdateHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;
import ru.itmo.tg.springbootcrud.labwork.model.Person;
import ru.itmo.tg.springbootcrud.labwork.model.UpdateHistory;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelDTOConverter {

    private final UserRepository userRepository;

    public DisciplineDTO convert(Discipline discipline) {
        return DisciplineDTO.builder()
                .id(discipline.getId())
                .name(discipline.getName())
                .lectureHours(discipline.getLectureHours())
                .ownerUsername(discipline.getOwner().getUsername())
                .build();
    }

    public PersonDTO convert(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .eyeColor(person.getEyeColor())
                .hairColor(person.getHairColor())
                .location(person.getLocation())
                .passportId(person.getPassportID())
                .nationality(person.getNationality())
                .ownerUsername(person.getOwner().getUsername())
                .build();
    }

    public LabWorkDTO convert(LabWork labWork) {
        return LabWorkDTO.builder()
                .id(labWork.getId())
                .name(labWork.getName())
                .coordinates(labWork.getCoordinates())
                .description(labWork.getDescription())
                .discipline(convert(labWork.getDiscipline()))
                .difficulty(labWork.getDifficulty())
                .minimalPoint(labWork.getMinimalPoint())
                .averagePoint(labWork.getAveragePoint())
                .author(convert(labWork.getAuthor()))
                .ownerUsername(labWork.getOwner().getUsername())
                .build();
    }

    public UpdateHistoryDTO convert(UpdateHistory updateHistory) {
        return UpdateHistoryDTO.builder()
                .id(updateHistory.getId())
                .labWorkId(updateHistory.getLabWorkID())
                .username(updateHistory.getUser().getUsername())
                .action(updateHistory.getAction())
                .actionTime(updateHistory.getActionTime())
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

    public Person convert(PersonDTO personDTO) {
        return Person.builder()
                .id(personDTO.getId())
                .eyeColor(personDTO.getEyeColor())
                .hairColor(personDTO.getHairColor())
                .location(personDTO.getLocation())
                .passportID(personDTO.getPassportId())
                .nationality(personDTO.getNationality())
                .owner(userRepository.findByUsername(personDTO.getOwnerUsername()).orElseThrow())
                .build();
    }

    public LabWork convert(LabWorkDTO labWorkDTO) {
        System.out.println(labWorkDTO.getId());
        return LabWork.builder()
                .id(labWorkDTO.getId())
                .name(labWorkDTO.getName())
                .coordinates(labWorkDTO.getCoordinates())
                .description(labWorkDTO.getDescription())
                .discipline(convert(labWorkDTO.getDiscipline()))
                .difficulty(labWorkDTO.getDifficulty())
                .minimalPoint(labWorkDTO.getMinimalPoint())
                .averagePoint(labWorkDTO.getAveragePoint())
                .author(convert(labWorkDTO.getAuthor()))
                .owner(userRepository.findByUsername(labWorkDTO.getOwnerUsername()).orElseThrow())
                .build();
    }

    public UpdateHistory convert(UpdateHistoryDTO updateHistoryDTO) {
        return UpdateHistory.builder()
                .id(updateHistoryDTO.getId())
                .labWorkID(updateHistoryDTO.getLabWorkId())
                .user(userRepository.findByUsername(updateHistoryDTO.getUsername()).orElseThrow())
                .action(updateHistoryDTO.getAction())
                .actionTime(updateHistoryDTO.getActionTime())
                .build();
    }

    public List<LabWorkDTO> toLabWorkDTOList(List<LabWork> labWorkList) {
        return labWorkList.stream().map(this::convert).toList();
    }

    public List<LabWork> toLabWorkList(List<LabWorkDTO> labWorkDTOList) {
        return labWorkDTOList.stream().map(this::convert).toList();
    }

    public List<PersonDTO> toPersonDTOList(List<Person> personList) {
        return personList.stream().map(this::convert).toList();
    }

    public List<Person> toPersonList(List<PersonDTO> personDTOList) {
        return personDTOList.stream().map(this::convert).toList();
    }

    public List<DisciplineDTO> toDisciplineDTOList(List<Discipline> disciplineList) {
        return disciplineList.stream().map(this::convert).toList();
    }

    public List<Discipline> toDisciplineList(List<DisciplineDTO> disciplineDTOList) {
        return disciplineDTOList.stream().map(this::convert).toList();
    }

    public List<UpdateHistoryDTO> toUpdateHistoryDTOList(List<UpdateHistory> updateHistoryList) {
        return updateHistoryList.stream().map(this::convert).toList();
    }

    public List<UpdateHistory> toUpdateHistoryList(List<UpdateHistoryDTO> updateHistoryDTOList) {
        return updateHistoryDTOList.stream().map(this::convert).toList();
    }

}
