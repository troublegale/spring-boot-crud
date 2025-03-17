package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.DisciplineNotFoundException;
import ru.itmo.tg.springbootcrud.labwork.exception.InsufficientPermissionsException;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.repository.DisciplineRepository;
import ru.itmo.tg.springbootcrud.labwork.validator.DisciplineValidator;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineValidator disciplineValidator;

    public List<DisciplineResponseDTO> getDisciplines(
            Integer pageNumber, Integer pageSize, String order, String sortCol) {
        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Order must be asc or desc");
        }
        Page<Discipline> page = disciplineRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return ModelDTOConverter.toDisciplineResponseDTOList(page.getContent());
    }

    public DisciplineResponseDTO getDisciplineById(Long id) {
        return ModelDTOConverter.convert(
                disciplineRepository.findById(id).orElseThrow(DisciplineNotFoundException::new));
    }

    @Transactional
    public DisciplineResponseDTO createDiscipline(DisciplineRequestDTO disciplineDTO, User user) {
        Discipline discipline = ModelDTOConverter.convert(disciplineDTO, user);
        disciplineValidator.validateDiscipline(discipline);
        discipline = disciplineRepository.save(discipline);
        return ModelDTOConverter.convert(discipline);
    }

    private int getHash(Discipline discipline) {
        return Objects.hash(discipline.getName(), discipline.getLectureHours());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int createDisciplines(List<Discipline> disciplines) {
        Set<Integer> uniqueHashes = new HashSet<>();
        List<Discipline> finalList = new ArrayList<>();
        for (Discipline discipline : disciplines) {
            disciplineValidator.validateDiscipline(discipline);
            if (!uniqueHashes.contains(getHash(discipline))) {
                uniqueHashes.add(getHash(discipline));
                finalList.add(discipline);
            }
        }
        disciplineRepository.saveAll(finalList);
        return finalList.size();
    }

    @Transactional
    public DisciplineResponseDTO updateDiscipline(Long id, DisciplineRequestDTO disciplineDTO, User user) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow(DisciplineNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !discipline.getOwner().equals(user)) {
            throw new InsufficientPermissionsException("no rights to edit Discipline #" + id);
        }
        discipline.setName(disciplineDTO.getName());
        discipline.setLectureHours(disciplineDTO.getLectureHours());
        discipline = disciplineRepository.save(discipline);
        return ModelDTOConverter.convert(discipline);
    }

    @Transactional
    public void deleteDiscipline(Long id, User user) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow(DisciplineNotFoundException::new);
        if (user.getRole() != Role.ROLE_ADMIN && !discipline.getOwner().equals(user)) {
            throw new InsufficientPermissionsException("no rights to delete Discipline #" + id);
        }
        disciplineRepository.delete(discipline);
    }

}
