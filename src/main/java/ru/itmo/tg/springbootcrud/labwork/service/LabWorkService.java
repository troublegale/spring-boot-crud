package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkDTO;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;
import ru.itmo.tg.springbootcrud.labwork.model.UpdateHistory;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Action;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;
import ru.itmo.tg.springbootcrud.labwork.repository.LabWorkRepository;
import ru.itmo.tg.springbootcrud.labwork.repository.UpdateHistoryRepository;
import ru.itmo.tg.springbootcrud.security.model.User;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;
    private final UpdateHistoryRepository updateHistoryRepository;
    private final ModelDTOConverter modelDTOConverter;

    public List<LabWorkDTO> getLabWorks(Integer pageNumber, Integer pageSize, String order, String sortCol) {
        Page<LabWork> page = labWorkRepository.findAll(PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(order), sortCol)));
        return modelDTOConverter.toLabWorkDTOList(page.getContent());
    }

    public LabWorkDTO getLabWorkById(Long id) {
        return modelDTOConverter.convert(labWorkRepository.findById(id).orElseThrow());
    }

    //TODO implement websocket message sending for every change

    public void createLabWork(LabWorkDTO labWorkDTO) {
        LabWork labWork = modelDTOConverter.convert(labWorkDTO);
        labWorkRepository.save(labWork);
        updateHistoryRepository.save(updateEntry(
                labWork.getId(), Action.CREATE,
                labWork.getOwner()
        ));
    }

    public void updateLabWork(Long id, LabWorkDTO labWorkDTO, User user) {
        labWorkDTO.setId(id);
        labWorkRepository.save(modelDTOConverter.convert(labWorkDTO));
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
        return modelDTOConverter.toLabWorkDTOList(labs);
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
        return modelDTOConverter.convert(lab);
    }

    private UpdateHistory updateEntry(Long labId, Action action, User actor) {
        return UpdateHistory.builder()
                .labWorkID(labId)
                .user(actor)
                .action(action)
                .build();
    }

}
