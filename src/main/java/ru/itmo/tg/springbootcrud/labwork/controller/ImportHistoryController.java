package ru.itmo.tg.springbootcrud.labwork.controller;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.tg.springbootcrud.labwork.dto.ImportHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.service.ImportHistoryService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/import-history")
@RequiredArgsConstructor
@Tag(name = "Import history", description = "History of imported XLSX files")
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ImportHistoryController {

    private final UserService userService;
    private final ImportHistoryService importHistoryService;

    @GetMapping
    public List<ImportHistoryDTO> getImportHistory() {
        return importHistoryService.getImportHistory(userService.getCurrentUser());
    }

}
