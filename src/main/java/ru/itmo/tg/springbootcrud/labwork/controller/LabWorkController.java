package ru.itmo.tg.springbootcrud.labwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.BadFileException;
import ru.itmo.tg.springbootcrud.labwork.exception.StorageException;
import ru.itmo.tg.springbootcrud.labwork.model.ImportHistory;
import ru.itmo.tg.springbootcrud.labwork.model.LabWork;
import ru.itmo.tg.springbootcrud.labwork.model.enums.ImportStatus;
import ru.itmo.tg.springbootcrud.labwork.service.FileProcessingService;
import ru.itmo.tg.springbootcrud.labwork.service.ImportHistoryService;
import ru.itmo.tg.springbootcrud.labwork.service.LabWorkService;
import ru.itmo.tg.springbootcrud.labwork.service.MinIOService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/lab-works")
@RequiredArgsConstructor
@Tag(name = "Lab Works")
public class LabWorkController {

    private final LabWorkService labWorkService;
    private final UserService userService;
    private final FileProcessingService fileProcessingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ImportHistoryService importHistoryService;
    private final MinIOService minIOService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all Lab Works",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Lab Works retrieved", content = @Content(schema = @Schema(implementation = LabWorkResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid parameters")
            }
    )
    public List<LabWorkResponseDTO> getLabWorks(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "100", name = "page-size", required = false) Integer pageSize,
            @RequestParam(defaultValue = "asc", required = false) String order,
            @RequestParam(defaultValue = "id", required = false) String sort) {
        return labWorkService.getLabWorks(page, pageSize, order, sort);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a Lab Work by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Lab Works retrieved", content = @Content(schema = @Schema(implementation = LabWorkResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Lab Work with stated ID was not found")
            }
    )
    public LabWorkResponseDTO getLabWorkById(@PathVariable Long id) {
        return labWorkService.getLabWorkById(id);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a Lab Work",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lab Work was created", content = @Content(schema = @Schema(implementation = LabWorkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "409", description = "Person with the same passportID already exists"),
                    @ApiResponse(responseCode = "404", description = "Nested objects with stated IDs not found")
            })
    public LabWorkResponseDTO createLabWork(@RequestBody LabWorkRequestDTO labWorkDTO) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.createLabWork(labWorkDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/labworks", Map.of("action", "create", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

    @PutMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a Lab Work",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lab Work was updated", content = @Content(schema = @Schema(implementation = LabWorkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "409", description = "Person with the same passportID already exists"),
                    @ApiResponse(responseCode = "404", description = "Nested objects or the Lab Work itself with stated ID not found")
            })
    public LabWorkResponseDTO updateLabWork(@PathVariable Long id, @RequestBody LabWorkRequestDTO labWorkDTO) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.updateLabWork(
                id, labWorkDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/labworks", Map.of("action", "update", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a Lab Work",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lab Work was deleted"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Lab Work with stated ID not found")
            })
    public void deleteLabWork(@PathVariable Long id) {
        labWorkService.deleteLabWork(id, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/labworks", Map.of("action", "delete", "value", id));
    }

    @DeleteMapping(value = "/delete-by-minimal-point", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Delete an assigned to current User Lab Work with stated minimal point value",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lab Work was deleted", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
            })
    public String deleteLabWorkByMinimalPoint(@RequestParam(name = "point") Integer minimalPoint) {
        Long result = labWorkService.deleteLabWorkByMinimalPoint(minimalPoint, userService.getCurrentUser());
        if (result > 0) {
            messagingTemplate.convertAndSend(
                    "/topic/labworks", Map.of("action", "delete", "value", result));
            return "Deleted LabWork #" + result;
        }
        return "There was no LabWork assigned to current user with such minimal point";
    }

    @GetMapping(value = "/count-by-author", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Get count of Lab Works with stated Author ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Count retrieved", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
            }
    )
    public String countLabWorkByAuthor(@RequestParam(name = "author-id") Long authorId) {
        Integer result = labWorkService.getCountByAuthorId(authorId);
        System.out.println(result);
        return result.toString();
    }

    @GetMapping(value = "/description-contains", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get an array of Lab Works with description containing stated substring",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Lab Works retrieved", content = @Content(schema = @Schema(implementation = LabWorkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
            }
    )
    public List<LabWorkResponseDTO> getLabWorksWithDescriptionContaining(
            @RequestParam String substring,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "20", name = "page-size", required = false) Integer pageSize) {
        return labWorkService.getLabWorksWithDescriptionContaining(substring, page, pageSize);
    }

    @PutMapping(value = "/{id}/adjust-difficulty", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Shift Lab Work's difficulty by a number of steps",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lab Work updated", content = @Content(schema = @Schema(implementation = LabWorkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Lab Work with stated ID not found")
            }
    )
    public LabWorkResponseDTO adjustDifficulty(@PathVariable Long id, @RequestParam Integer by) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.adjustDifficulty(
                id, by, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/labworks", Map.of("action", "update", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

    @PostMapping(value = "/{id}/copy-to-discipline", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Copy a LabWork with stated Discipline ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lab Work copied", content = @Content(schema = @Schema(implementation = LabWorkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Discipline with stated ID not found")
            }
    )
    public LabWorkResponseDTO copyLabWorkToDiscipline(@PathVariable Long id,
                                                      @RequestParam(name = "discipline-id") Long disciplineId) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.copyLabWorkToDiscipline(
                id, disciplineId, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/labworks", Map.of("action", "create", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

    @PostMapping("/import")
    @Operation(summary = "Import a .xlsx file",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "LabWorks were created", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation, bad file", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    public String importLabWorks(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return "empty file";
        }

        String contentType = file.getContentType();
        if (!Objects.equals(contentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return ".xlsx file required";
        }

        String fileNameInMinio = file.getOriginalFilename() + "_" + UUID.randomUUID();

        ImportHistory importHistory = ImportHistory.builder()
                .fileName(file.getOriginalFilename())
                .fileNameMinio(fileNameInMinio)
                .user(userService.getCurrentUser())
                .importStatus(ImportStatus.ERROR)
                .importNumber(0)
                .build();

        try {
            importHistoryService.save(importHistory);
        } catch (Exception e) {
            return "DB error";
        }

        try {
            fileProcessingService.processFile(file, LabWork.class, fileNameInMinio, importHistory);
        } catch (BadFileException e) {
            return "Failed to import, bad file: " + e.getMessage();
        } catch (StorageException e) {
            return "Failed to import, storage error: " + e.getMessage();
        } catch (Exception e) {
            try {
                minIOService.deleteFile(fileNameInMinio);
            } catch (Exception ignored) {
                return "Failed to import, storage error: DB & MinIO Error";
            }
            return "Failed to import, storage error: DB Error";
        }

        try {
            importHistory.setImportStatus(ImportStatus.IMPORTED);
            importHistoryService.save(importHistory);
        } catch (Exception e) {
            throw new StorageException("DB error");
        }

        return "File imported successfully";
    }

}
