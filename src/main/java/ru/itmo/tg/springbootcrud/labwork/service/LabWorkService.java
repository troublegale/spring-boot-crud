package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonDTO;
import ru.itmo.tg.springbootcrud.labwork.model.*;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Action;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;
import ru.itmo.tg.springbootcrud.labwork.repository.LabWorkRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.UpdateHistoryRepository;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;
    private final UserRepository userRepository;
    private final UpdateHistoryRepository updateHistoryRepository;

    public List<LabWorkDTO> getLabWorks(Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<LabWork> page = labWorkRepository.findAll(PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return toDTO(page.getContent());
    }

    public LabWorkDTO getLabWorkById(Long id) {
        return toDTO(labWorkRepository.findById(id).orElseThrow());
    }

    public void createLabWork(LabWorkDTO labWorkDTO) {
        LabWork labWork = toEntity(labWorkDTO);
        labWorkRepository.save(labWork);
        updateHistoryRepository.save(updateEntry(
                labWork.getId(), Action.CREATE,
                labWork.getOwner()
        ));
    }

    public void updateLabWork(Long id, LabWorkDTO labWorkDTO, User user) {
        labWorkDTO.setId(id);
        labWorkRepository.save(toEntity(labWorkDTO));
        updateHistoryRepository.save(updateEntry(labWorkDTO.getId(), Action.UPDATE, user));
    }

    public void deleteLabWork(Long id, User user) {
        labWorkRepository.deleteById(id);
        updateHistoryRepository.save(updateEntry(id, Action.DELETE, user));
    }

    public Boolean deleteLabWorkByMinimalPoint(Integer p, User user) {
        return labWorkRepository.deleteLabWorkByMinimalPoint(p, user.getId());
    }

    public Integer getCountByAuthorId(Long authorId) {
        return labWorkRepository.getCountByAuthorId(authorId);
    }

    public List<LabWorkDTO> getLabWorksWithDescriptionContaining(String substring, Integer page, Integer pageSize) {
        var labs = labWorkRepository.getLabWorksWithDescriptionContaining(substring, page - 1, pageSize);
        labs.forEach(System.out::println);
        return toDTO(labs);
    }

    public Difficulty adjustDifficulty(Long id, Integer steps) {
        String[] diffStringVals = Arrays.stream(Difficulty.values())
                .map(Difficulty::toString)
                .toList().toArray(String[]::new);
        String diffVal = labWorkRepository.adjustDifficulty(id, steps, diffStringVals);
        return Difficulty.valueOf(diffVal);
    }

    public LabWorkDTO copyLabWorkToDiscipline(Long labId, Long disciplineId, User user) {
        LabWork lab = labWorkRepository.copyLabWorkToDiscipline(labId, disciplineId, user.getId());
        return toDTO(lab);
    }

    private UpdateHistory updateEntry(Long labId, Action action, User actor) {
        return UpdateHistory.builder()
                .labWorkID(labId)
                .user(actor)
                .action(action)
                .build();
    }

    private List<LabWorkDTO> toDTO(List<LabWork> labWorks) {
        return labWorks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private LabWorkDTO toDTO(LabWork labWork) {
        return LabWorkDTO.builder()
                .id(labWork.getId())
                .name(labWork.getName())
                .x(labWork.getCoordinates().getX())
                .y(labWork.getCoordinates().getY())
                .difficulty(labWork.getDifficulty())
                .description(labWork.getDescription())
                .disciplineDTO(toDTO(labWork.getDiscipline()))
                .minimalPoint(labWork.getMinimalPoint())
                .averagePoint(labWork.getAveragePoint())
                .personDTO(toDTO(labWork.getAuthor()))
                .ownerUsername(labWork.getOwner().getUsername())
                .build();
    }

    private DisciplineDTO toDTO(Discipline discipline) {
        return DisciplineDTO.builder()
                .id(discipline.getId())
                .name(discipline.getName())
                .lectureHours(discipline.getLectureHours())
                .ownerUsername(discipline.getOwner().getUsername())
                .build();
    }

    private PersonDTO toDTO(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .eyeColor(person.getEyeColor())
                .hairColor(person.getHairColor())
                .x(person.getLocation().getX())
                .y(person.getLocation().getY())
                .z(person.getLocation().getZ())
                .passportID(person.getPassportID())
                .nationality(person.getNationality())
                .ownerUsername(person.getOwner().getUsername())
                .build();
    }

    private Discipline toEntity(DisciplineDTO disciplineDTO) {
        Discipline discipline = Discipline.builder()
                .name(disciplineDTO.getName())
                .lectureHours(disciplineDTO.getLectureHours())
                .owner(userRepository.findByUsername(disciplineDTO.getOwnerUsername()).orElseThrow())
                .build();
        if (disciplineDTO.getId() != null) {
            discipline.setId(disciplineDTO.getId());
        }
        return discipline;
    }

    private Person toEntity(PersonDTO personDTO) {
        Person person = Person.builder()
                .eyeColor(personDTO.getEyeColor())
                .hairColor(personDTO.getHairColor())
                .location(Location.builder()
                        .x(personDTO.getX())
                        .y(personDTO.getY())
                        .z(personDTO.getZ())
                        .build())
                .passportID(personDTO.getPassportID())
                .nationality(personDTO.getNationality())
                .owner(userRepository.findByUsername(personDTO.getOwnerUsername()).orElseThrow())
                .build();
        if (personDTO.getId() != null) {
            person.setId(personDTO.getId());
        }
        return person;
    }

    private LabWork toEntity(LabWorkDTO labWorkDTO) {
        LabWork labWork = LabWork.builder()
                .name(labWorkDTO.getName())
                .coordinates(Coordinates.builder()
                        .x(labWorkDTO.getX())
                        .y(labWorkDTO.getY())
                        .build())
                .creationDate(Date.from(Instant.now()))
                .description(labWorkDTO.getDescription())
                .difficulty(labWorkDTO.getDifficulty())
                .discipline(toEntity(labWorkDTO.getDisciplineDTO()))
                .minimalPoint(labWorkDTO.getMinimalPoint())
                .averagePoint(labWorkDTO.getAveragePoint())
                .author(toEntity(labWorkDTO.getPersonDTO()))
                .owner(userRepository.findByUsername(labWorkDTO.getOwnerUsername()).orElseThrow())
                .build();
        if (labWorkDTO.getId() != null) {
            labWork.setId(labWorkDTO.getId());
        }
        return labWork;
    }

}
