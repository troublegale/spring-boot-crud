package ru.itmo.tg.springbootcrud.labwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.model.Discipline;
import ru.itmo.tg.springbootcrud.labwork.service.DisciplineService;
import ru.itmo.tg.springbootcrud.labwork.service.FileProcessingService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/disciplines")
@RequiredArgsConstructor
@Tag(name = "Disciplines", description = "CRUD operations for managing Disciplines")
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class DisciplineController {

    private final DisciplineService disciplineService;
    private final UserService userService;
    private final FileProcessingService fileProcessingService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all Disciplines",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Disciplines retrieved", content = @Content(schema = @Schema(implementation = DisciplineResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid parameters")
            }
    )
    public List<DisciplineResponseDTO> getDisciplines(
            @RequestParam(defaultValue = "1") @Parameter(description = "Number of the page") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") @Parameter(description = "Size of the page") Integer pageSize,
            @RequestParam(defaultValue = "asc") @Parameter(description = "Sorting order (asc or desc)") String order,
            @RequestParam(defaultValue = "id") @Parameter(description = "Sorting column (has to be one of the fields of a Discipline") String sort) {
        return disciplineService.getDisciplines(page, pageSize, order, sort);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Discipline by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Discipline object retrieved", content = @Content(schema = @Schema(implementation = DisciplineResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Discipline with stated ID wasn't found", content = @Content(schema = @Schema(implementation = String.class)))
            })
    public DisciplineResponseDTO getDisciplineById(@PathVariable Long id) {
        return disciplineService.getDisciplineById(id);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a Discipline",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Discipline was created", content = @Content(schema = @Schema(implementation = DisciplineResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation", content = @Content(schema = @Schema(implementation = String.class)))
            })
    public DisciplineResponseDTO createDiscipline(@RequestBody DisciplineRequestDTO disciplineDTO) {
        DisciplineResponseDTO disciplineResponseDTO = disciplineService.createDiscipline(
                disciplineDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/disciplines", Map.of("action", "create", "value", disciplineResponseDTO));
        return disciplineResponseDTO;
    }

    @PutMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a Discipline",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Discipline was updated", content = @Content(schema = @Schema(implementation = DisciplineResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Object with this ID does not exist", content = @Content(schema = @Schema(implementation = String.class))),
            })
    public DisciplineResponseDTO updateDiscipline(@PathVariable Long id,
                                                  @RequestBody DisciplineRequestDTO disciplineDTO) {
        DisciplineResponseDTO disciplineResponseDTO = disciplineService.updateDiscipline(
                id, disciplineDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/disciplines", Map.of("action", "update", "value", disciplineResponseDTO));
        return disciplineResponseDTO;
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a Discipline",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Discipline was deleted"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Object with this ID does not exist", content = @Content(schema = @Schema(implementation = String.class))),
            })
    public void deleteDiscipline(@PathVariable Long id) {
        disciplineService.deleteDiscipline(id, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/disciplines", Map.of("action", "delete", "value", id));
    }

    @PostMapping("/import")
    @Operation(summary = "Import a .xlsx file",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Disciplines were created", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation, bad file", content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    public String importDisciplines(MultipartFile file) {
        fileProcessingService.processFile(file, Discipline.class);
        return "File imported successfully";
    }
}
