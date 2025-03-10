package ru.itmo.tg.springbootcrud.labwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.dto.ImportHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.repository.ImportHistoryRepository;
import ru.itmo.tg.springbootcrud.misc.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportHistoryService {

    private final ImportHistoryRepository importHistoryRepository;

    public List<ImportHistoryDTO> getImportHistory(User user) {
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            return ModelDTOConverter.toImportHistoryDTOList(importHistoryRepository.findAll());
        }
        return ModelDTOConverter.toImportHistoryDTOList(importHistoryRepository.findByUser(user));
    }
}
