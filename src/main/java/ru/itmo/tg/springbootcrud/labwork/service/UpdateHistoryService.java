package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.UpdateHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.LabWorkNotFoundException;
import ru.itmo.tg.springbootcrud.labwork.repository.UpdateHistoryRepository;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateHistoryService {

    private final UpdateHistoryRepository updateHistoryRepository;

    public List<UpdateHistoryDTO> getAllUpdateHistory() {
        return ModelDTOConverter.toUpdateHistoryDTOList(updateHistoryRepository.findAll());
    }

    public UpdateHistoryDTO getLabWorkUpdateHistory(long labId) {
        return ModelDTOConverter.convert(
                updateHistoryRepository.findByLabWorkID(labId).orElseThrow(LabWorkNotFoundException::new));
    }

}
