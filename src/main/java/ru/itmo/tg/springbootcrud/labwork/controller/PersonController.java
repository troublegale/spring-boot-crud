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
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.service.PersonService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
@Tag(name = "Persons")
public class PersonController {

    private final PersonService personService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all Persons",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Persons retrieved", content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid parameters")
            }
    )
    public List<PersonResponseDTO> getPersons(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") Integer pageSize,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "id") String sort) {
        return personService.getPersons(page, pageSize, order, sort);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Person by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person object retrieved", content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Person with stated ID not found")
            }
    )
    public PersonResponseDTO getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a Person",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person was created", content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "409", description = "Person with the same passportID already exists")
            })
    public PersonResponseDTO createPerson(@RequestBody PersonRequestDTO personDTO) {
        PersonResponseDTO personResponseDTO = personService.createPerson(personDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/persons", Map.of("action", "create", "value", personResponseDTO));
        return personResponseDTO;
    }

    @PutMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a Person",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person was updated", content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid content, constraint violation", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "409", description = "Person with the same passportID already exists"),
                    @ApiResponse(responseCode = "404", description = "Person with stated ID was not found")
            })
    public PersonResponseDTO updatePerson(@PathVariable Long id, @RequestBody PersonRequestDTO personDTO) {
        PersonResponseDTO personResponseDTO = personService.updatePerson(
                id, personDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/persons", Map.of("action", "update", "value", personResponseDTO));
        return personResponseDTO;
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a Person",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person was deleted"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Person with stated ID not found")
            })
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/persons", Map.of("action", "delete", "value", id));
    }
}
