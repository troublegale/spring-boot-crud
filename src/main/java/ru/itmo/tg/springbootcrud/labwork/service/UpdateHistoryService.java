package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.UpdateHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.repository.UpdateHistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateHistoryService {

    private final UpdateHistoryRepository updateHistoryRepository;
    private final ModelDTOConverter modelDTOConverter;

    public List<UpdateHistoryDTO> getAllUpdateHistory() {
        return modelDTOConverter.toUpdateHistoryDTOList(updateHistoryRepository.findAll());
    }

    public UpdateHistoryDTO getLabWorkUpdateHistory(long labId) {
        return modelDTOConverter.convert(updateHistoryRepository.findByLabWorkID(labId).orElseThrow());
    }

}
