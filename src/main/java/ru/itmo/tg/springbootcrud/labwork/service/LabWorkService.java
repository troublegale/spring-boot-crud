package ru.itmo.tg.springbootcrud.labwork.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.*;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;
import ru.itmo.tg.springbootcrud.labwork.model.Person;
import ru.itmo.tg.springbootcrud.labwork.model.UpdateHistory;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Action;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;
import ru.itmo.tg.springbootcrud.labwork.repository.DisciplineRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.LabWorkRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.PersonRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.UpdateHistoryRepository;
import ru.itmo.tg.springbootcrud.labwork.validator.LabWorkValidator;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;
    private final DisciplineRepository disciplineRepository;
    private final PersonRepository personRepository;
    private final UpdateHistoryRepository updateHistoryRepository;
    private final LabWorkValidator labWorkValidator;

    private final ConcurrentHashMap<String, Object> uniqueLabs = new ConcurrentHashMap<>();
    private final static Object BOB = new Object();

    @PostConstruct
    private void initKeys() {
        labWorkRepository.findAll().forEach(labWork ->
                uniqueLabs.put(getKey(labWork.getName(), labWork.getDescription()), BOB));
    }

    private String getKey(String name, String description) {
        return name + "::" + description;
    }

    public List<LabWorkResponseDTO> getLabWorks(Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<LabWork> page = labWorkRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return ModelDTOConverter.toLabWorkResponseDTOList(page.getContent());
    }

    public LabWorkResponseDTO getLabWorkById(Long id) {
        return ModelDTOConverter.convert(labWorkRepository.findById(id).orElseThrow(LabWorkNotFoundException::new));
    }

    @Transactional
    public LabWorkResponseDTO createLabWork(LabWorkRequestDTO labWorkDTO, User user) {
        String key = getKey(labWorkDTO.getName(), labWorkDTO.getDescription());
        if (uniqueLabs.containsKey(key)) {
            throw new UniqueAttributeException("LabWork with such name and description already exists");
        }

        if ((labWorkDTO.getDiscipline() == null && labWorkDTO.getDisciplineId() == null)
                || (labWorkDTO.getAuthor() == null && labWorkDTO.getAuthorId() == null)) {
            throw new AbsentNestedObjectsException();
        }
        LabWork labWork = ModelDTOConverter.convert(labWorkDTO, user);
        labWorkValidator.validateLabWork(labWork);
        if (labWork.getDiscipline() == null) {
            labWork.setDiscipline(disciplineRepository.findById(
                    labWorkDTO.getDisciplineId()).orElseThrow(DisciplineNotFoundException::new));
        }
        if (labWork.getAuthor() == null) {
            labWork.setAuthor(personRepository.findById(
                    labWorkDTO.getAuthorId()).orElseThrow(PersonNotFoundException::new));
        } else if (personRepository.existsByPassportID(labWork.getAuthor().getPassportID())) {
            throw new UniqueAttributeException("this passport already exists");
        }
        labWork = labWorkRepository.save(labWork);
        uniqueLabs.put(key, BOB);
        updateHistoryRepository.save(updateEntry(labWork.getId(), Action.CREATE, user));
        return ModelDTOConverter.convert(labWork);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int createLabWorks(List<LabWork> labWorks) {
        for (LabWork labWork : labWorks) {
            labWorkValidator.validateLabWork(labWork);
            String key = getKey(labWork.getName(), labWork.getDescription());
            if (uniqueLabs.containsKey(key)) {
                throw new UniqueAttributeException("LabWork with such name and description already exists");
            }
            if (personRepository.existsByPassportID(labWork.getAuthor().getPassportID())) {
                throw new UniqueAttributeException("this passport already exists");
            }
        }
        labWorkRepository.saveAll(labWorks);
        labWorks.forEach(labWork -> uniqueLabs.put(getKey(labWork.getName(), labWork.getDescription()), BOB));
        return labWorks.size();
    }

    @Transactional
    public LabWorkResponseDTO updateLabWork(Long id, LabWorkRequestDTO labWorkDTO, User user) {
        LabWork labWork = labWorkRepository.findById(id).orElseThrow(LabWorkNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !labWork.getOwner().equals(user)) {
            throw new InsufficientPermissionsException("no rights to edit LabWork #" + id);
        }
        labWork.setName(labWorkDTO.getName());
        labWork.setCoordinates(labWorkDTO.getCoordinates());
        labWork.setDescription(labWorkDTO.getDescription());
        labWork.setDifficulty(labWorkDTO.getDifficulty());
        labWork.setMinimalPoint(labWorkDTO.getMinimalPoint());
        labWork.setAveragePoint(labWorkDTO.getAveragePoint());

        if (labWorkDTO.getDisciplineId() != null) {
            labWork.setDiscipline(disciplineRepository.findById(
                    labWorkDTO.getDisciplineId()).orElseThrow(DisciplineNotFoundException::new));
        }
        if (labWorkDTO.getDiscipline() != null) {
            Discipline discipline = labWork.getDiscipline();
            if (user.getRole() != Role.ROLE_ADMIN && !discipline.getOwner().equals(user)) {
                throw new InsufficientPermissionsException("no rights to edit Discipline #" + discipline.getId());
            }
            DisciplineRequestDTO disciplineDTO = labWorkDTO.getDiscipline();
            discipline.setName(disciplineDTO.getName());
            discipline.setLectureHours(disciplineDTO.getLectureHours());
            labWork.setDiscipline(discipline);
        }

        if (labWorkDTO.getAuthorId() != null) {
            labWork.setAuthor(personRepository.findById(
                    labWorkDTO.getAuthorId()).orElseThrow(PersonNotFoundException::new));
        }
        if (labWorkDTO.getAuthor() != null) {
            Person person = labWork.getAuthor();
            if (user.getRole() != Role.ROLE_ADMIN && !person.getOwner().equals(user)) {
                throw new InsufficientPermissionsException("no rights to edit Person #" + person.getId());
            }
            if (!person.getPassportID().equals(labWorkDTO.getAuthor().getPassportId())) {
                if (personRepository.existsByPassportID(labWorkDTO.getAuthor().getPassportId())) {
                    throw new UniqueAttributeException("this passport already exists");
                }
            }
            PersonRequestDTO personDTO = labWorkDTO.getAuthor();
            person.setName(personDTO.getName());
            person.setEyeColor(personDTO.getEyeColor());
            person.setHairColor(personDTO.getHairColor());
            person.setLocation(personDTO.getLocation());
            person.setPassportID(personDTO.getPassportId());
            person.setNationality(personDTO.getNationality());
            labWork.setAuthor(person);
        }

        labWork = labWorkRepository.save(labWork);
        updateHistoryRepository.save(updateEntry(id, Action.UPDATE, user));
        return ModelDTOConverter.convert(labWork);
    }

    @Transactional
    public void deleteLabWork(Long id, User user) {
        LabWork labWork = labWorkRepository.findById(id).orElseThrow(LabWorkNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !labWork.getOwner().equals(user)) {
            throw new InsufficientPermissionsException("no rights to edit LabWork #" + id);
        }
        labWorkRepository.deleteById(id);
        updateHistoryRepository.save(updateEntry(id, Action.DELETE, user));
    }

    @Transactional
    public Long deleteLabWorkByMinimalPoint(Integer p, User user) {
        Long id = labWorkRepository.deleteLabWorkByMinimalPoint(p, user.getId());
        if (id > 0) {
            updateHistoryRepository.save(updateEntry(id, Action.DELETE, user));
        }
        return id;
    }

    public Integer getCountByAuthorId(Long authorId) {
        Integer result = labWorkRepository.getCountByAuthorId(authorId);
        System.out.println(result);
        return result;
    }

    public List<LabWorkResponseDTO> getLabWorksWithDescriptionContaining(String substring, Integer page, Integer pageSize) {
        var labs = labWorkRepository.getLabWorksWithDescriptionContaining(substring, page - 1, pageSize);
        return ModelDTOConverter.toLabWorkResponseDTOList(labs);
    }

    @Transactional
    public LabWorkResponseDTO adjustDifficulty(Long id, Integer steps, User user) {
        LabWork labWork = labWorkRepository.findById(id).orElseThrow(LabWorkNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !labWork.getOwner().equals(user)) {
            throw new InsufficientPermissionsException("no rights to edit LabWork #" + id);
        }
        String[] diffStringVals = Arrays.stream(Difficulty.values())
                .map(Difficulty::toString)
                .toList().toArray(String[]::new);
        String diffVal = labWorkRepository.adjustDifficulty(id, steps, diffStringVals);
        labWork.setDifficulty(Difficulty.valueOf(diffVal));
        updateHistoryRepository.save(updateEntry(id, Action.UPDATE, user));
        return ModelDTOConverter.convert(labWork);
    }

    @Transactional
    public LabWorkResponseDTO copyLabWorkToDiscipline(Long labId, Long disciplineId, User user) {
        LabWork labWork = labWorkRepository.copyLabWorkToDiscipline(labId, disciplineId, user.getId());
        updateHistoryRepository.save(updateEntry(labWork.getId(), Action.CREATE, user));
        return ModelDTOConverter.convert(labWork);
    }

    private UpdateHistory updateEntry(Long labId, Action action, User actor) {
        return UpdateHistory.builder()
                .labWorkID(labId)
                .user(actor)
                .action(action)
                .build();
    }

}
