package ru.itmo.tg.springbootcrud.labwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkDTO;
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
            @RequestParam(defaultValue = "desc") String order,
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

}
