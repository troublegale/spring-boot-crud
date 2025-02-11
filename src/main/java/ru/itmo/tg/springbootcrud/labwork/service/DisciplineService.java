package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.InsufficientPermissionsException;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.repository.DisciplineRepository;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final ModelDTOConverter modelDTOConverter;

    public List<DisciplineResponseDTO> getDisciplines(
            Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<Discipline> page = disciplineRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return modelDTOConverter.toDisciplineResponseDTOList(page.getContent());
    }

    public DisciplineResponseDTO getDisciplineById(Long id) {
        return modelDTOConverter.convert(disciplineRepository.findById(id).orElseThrow());
    }

    //TODO implement websocket message sending for every change

    public DisciplineResponseDTO createDiscipline(DisciplineRequestDTO disciplineDTO, User user) {
        Discipline discipline = modelDTOConverter.convert(disciplineDTO, user);
        discipline = disciplineRepository.save(discipline);
        return modelDTOConverter.convert(discipline);
    }

    public DisciplineResponseDTO updateDiscipline(Long id, DisciplineRequestDTO disciplineDTO, User user) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow();
        if (user.getRole() != Role.ROLE_ADMIN && !discipline.getOwner().equals(user)) {
            throw new InsufficientPermissionsException("no rights to edit Discipline #" + id);
        }
        discipline.setName(disciplineDTO.getName());
        discipline.setLectureHours(disciplineDTO.getLectureHours());
        discipline = disciplineRepository.save(discipline);
        return modelDTOConverter.convert(discipline);
    }

    public void deleteDiscipline(Long id, User user) {
        Discipline discipline = disciplineRepository.findById(id).orElseThrow();
        if (user.getRole() != Role.ROLE_ADMIN && !discipline.getOwner().equals(user)) {
            throw new InsufficientPermissionsException("no rights to delete Discipline #" + id);
        }
        disciplineRepository.delete(discipline);
    }

}
