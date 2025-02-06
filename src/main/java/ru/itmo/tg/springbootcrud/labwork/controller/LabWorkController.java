package ru.itmo.tg.springbootcrud.labwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkDTO;
import ru.itmo.tg.springbootcrud.labwork.model.enums.Difficulty;
import ru.itmo.tg.springbootcrud.labwork.service.LabWorkService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/lab-works")
@RequiredArgsConstructor
public class LabWorkController {

    private final LabWorkService labWorkService;
    private final UserService userService;

    @GetMapping
    public List<LabWorkDTO> getLabWorks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") Integer pageSize,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "id") String sort) {
        return labWorkService.getLabWorks(page, pageSize, order, sort);
    }

    @GetMapping("/{id}")
    public LabWorkDTO getLabWorkById(@PathVariable Long id) {
        return labWorkService.getLabWorkById(id);
    }

    @PostMapping("/create")
    public void createLabWork(@RequestBody LabWorkDTO labWorkDTO) {
        labWorkService.createLabWork(labWorkDTO);
    }

    @PutMapping("/{id}/update")
    public void updateLabWork(@PathVariable Long id, @RequestBody LabWorkDTO labWorkDTO) {
        labWorkService.updateLabWork(id, labWorkDTO, userService.getCurrentUser());
    }

    @DeleteMapping("/{id}/delete")
    public void deleteLabWork(@PathVariable Long id) {
        labWorkService.deleteLabWork(id, userService.getCurrentUser());
    }

    @DeleteMapping("/delete-by-minimal-point")
    public Boolean deleteLabWorkByMinimalPoint(@RequestParam(name = "p") Integer minimalPoint) {
        return labWorkService.deleteLabWorkByMinimalPoint(minimalPoint, userService.getCurrentUser());
    }

    @GetMapping("/count-by-author")
    public Integer countLabWorkByAuthor(@RequestParam(name = "author-id") Long authorId) {
        return labWorkService.getCountByAuthorId(authorId);
    }

    @GetMapping("/description-contains")
    public List<LabWorkDTO> getLabWorksWithDescriptionContaining(
            @RequestParam String substring,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") Integer pageSize) {
        return labWorkService.getLabWorksWithDescriptionContaining(substring, page, pageSize);
    }

    @PutMapping("/{id}/adjust-difficulty")
    public Difficulty adjustDifficulty(@PathVariable Long id, @RequestParam Integer by) {
        return labWorkService.adjustDifficulty(id, by);
    }

    @PostMapping("/{id}/copy-to-discipline")
    public LabWorkDTO copyLabWorkToDiscipline(@PathVariable Long id,
                                              @RequestParam(name = "discipline-id") Long disciplineId) {
        return labWorkService.copyLabWorkToDiscipline(id, disciplineId, userService.getCurrentUser());
    }

}
