package ru.itmo.tg.springbootcrud.labwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineDTO;
import ru.itmo.tg.springbootcrud.labwork.service.DisciplineService;

import java.util.List;

@RestController
@RequestMapping("/disciplines")
@RequiredArgsConstructor
public class DisciplineController {

    private final DisciplineService disciplineService;

    @GetMapping
    public List<DisciplineDTO> getDisciplines(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") Integer pageSize,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "id") String sort) {
        return disciplineService.getDisciplines(page, pageSize, order, sort);
    }

    @GetMapping("/{id}")
    public DisciplineDTO getDisciplineById(@PathVariable Long id) {
        return disciplineService.getDisciplineById(id);
    }

    @PostMapping("/create")
    public void createDiscipline(@RequestBody DisciplineDTO disciplineDTO) {
        disciplineService.createDiscipline(disciplineDTO);
    }

    @PutMapping("/{id}/update")
    public void updateDiscipline(@PathVariable Long id, @RequestBody DisciplineDTO disciplineDTO) {
        disciplineService.updateDiscipline(id, disciplineDTO);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteDiscipline(@PathVariable Long id) {
        disciplineService.deleteDiscipline(id);
    }

}
