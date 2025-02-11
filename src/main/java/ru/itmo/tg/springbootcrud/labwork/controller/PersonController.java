package ru.itmo.tg.springbootcrud.labwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.service.PersonService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public List<PersonResponseDTO> getPersons(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") Integer pageSize,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "id") String sort) {
        return personService.getPersons(page, pageSize, order, sort);
    }

    @GetMapping("/{id}")
    public PersonResponseDTO getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping("/create")
    public PersonResponseDTO createPerson(@RequestBody PersonRequestDTO personDTO) {
        PersonResponseDTO personResponseDTO = personService.createPerson(personDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/persons", Map.of("action", "create", "value", personResponseDTO));
        return personResponseDTO;
    }

    @PutMapping("/{id}/update")
    public PersonResponseDTO updatePerson(@PathVariable Long id, @RequestBody PersonRequestDTO personDTO) {
        PersonResponseDTO personResponseDTO = personService.updatePerson(
                id, personDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/persons", Map.of("action", "update", "value", personResponseDTO));
        return personResponseDTO;
    }

    @DeleteMapping("/{id}/delete")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/persons", Map.of("action", "delete", "value", id));
    }
}
