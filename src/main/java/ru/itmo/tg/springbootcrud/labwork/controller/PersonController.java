package ru.itmo.tg.springbootcrud.labwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.PersonDTO;
import ru.itmo.tg.springbootcrud.labwork.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<PersonDTO> getPersons(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") Integer pageSize,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "id") String sort) {
        return personService.getPersons(page, pageSize, order, sort);
    }

    @GetMapping("/{id}")
    public PersonDTO getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping("/create")
    public void createPerson(@RequestBody PersonDTO personDTO) {
        personService.createPerson(personDTO);
    }

    @PutMapping("/{id}/update")
    public void updatePerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        personService.updatePerson(id, personDTO);
    }

    @DeleteMapping("/{id}/delete")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }
}
