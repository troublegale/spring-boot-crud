package ru.itmo.tg.springbootcrud.labwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.service.DisciplineService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/disciplines")
@RequiredArgsConstructor
public class DisciplineController {

    private final DisciplineService disciplineService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public List<DisciplineResponseDTO> getDisciplines(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20", name = "page-size") Integer pageSize,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "id") String sort) {
        return disciplineService.getDisciplines(page, pageSize, order, sort);
    }

    @GetMapping("/{id}")
    public DisciplineResponseDTO getDisciplineById(@PathVariable Long id) {
        return disciplineService.getDisciplineById(id);
    }

    @PostMapping("/create")
    public DisciplineResponseDTO createDiscipline(@RequestBody DisciplineRequestDTO disciplineDTO) {
        DisciplineResponseDTO disciplineResponseDTO = disciplineService.createDiscipline(
                disciplineDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/disciplines", Map.of("action", "create", "value", disciplineResponseDTO));
        return disciplineResponseDTO;
    }

    @PutMapping("/{id}/update")
    public DisciplineResponseDTO updateDiscipline(@PathVariable Long id,
                                                  @RequestBody DisciplineRequestDTO disciplineDTO) {
        DisciplineResponseDTO disciplineResponseDTO = disciplineService.updateDiscipline(
                id, disciplineDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/disciplines", Map.of("action", "update", "value", disciplineResponseDTO));
        return disciplineResponseDTO;
    }

    @DeleteMapping("/{id}/delete")
    public void deleteDiscipline(@PathVariable Long id) {
        disciplineService.deleteDiscipline(id, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "/topic/disciplines", Map.of("action", "delete", "value", id));
    }

}
