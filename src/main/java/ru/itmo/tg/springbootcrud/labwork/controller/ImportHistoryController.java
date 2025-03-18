package ru.itmo.tg.springbootcrud.labwork.controller;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.tg.springbootcrud.labwork.dto.ImportHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.ImportHistoryNotFoundException;
import ru.itmo.tg.springbootcrud.labwork.model.ImportHistory;
import ru.itmo.tg.springbootcrud.labwork.service.ImportHistoryService;
import ru.itmo.tg.springbootcrud.labwork.service.MinIOService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.io.InputStream;
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
    private final MinIOService minIOService;

    @GetMapping
    public List<ImportHistoryDTO> getImportHistory() {
        return importHistoryService.getImportHistory(userService.getCurrentUser());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) {
        ImportHistory importHistory = importHistoryService.getImportHistory(id);
        try {
            InputStream inputStream = minIOService.getFile(importHistory.getFileNameMinio());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + importHistory + "\"")
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
