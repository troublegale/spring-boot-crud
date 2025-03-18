package ru.itmo.tg.springbootcrud.labwork.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.tg.springbootcrud.labwork.dto.ImportHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.ImportHistoryNotFoundException;
import ru.itmo.tg.springbootcrud.labwork.model.ImportHistory;
import ru.itmo.tg.springbootcrud.labwork.model.enums.ImportStatus;
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

    public ImportHistory getImportHistory(Long id) {
        return importHistoryRepository.findById(id).orElseThrow(() ->
                new ImportHistoryNotFoundException("file not found"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ImportHistory importHistory) {
        importHistoryRepository.save(importHistory);
    }

}
