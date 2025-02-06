package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineDTO;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.repository.DisciplineRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final ModelDTOConverter modelDTOConverter;

    public List<DisciplineDTO> getDisciplines(Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<Discipline> page = disciplineRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return modelDTOConverter.toDisciplineDTOList(page.getContent());
    }

    public DisciplineDTO getDisciplineById(Long id) {
        return modelDTOConverter.convert(disciplineRepository.findById(id).orElseThrow());
    }

    //TODO implement websocket message sending for every change

    public void createDiscipline(DisciplineDTO disciplineDTO) {
        Discipline discipline = modelDTOConverter.convert(disciplineDTO);
        disciplineRepository.save(discipline);
    }

    public void updateDiscipline(Long id, DisciplineDTO disciplineDTO) {
        disciplineDTO.setId(id);
        disciplineRepository.save(modelDTOConverter.convert(disciplineDTO));
    }

    public void deleteDiscipline(Long id) {
        disciplineRepository.deleteById(id);
    }

}
