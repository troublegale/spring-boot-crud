package ru.itmo.tg.springbootcrud.labwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkRequestDTO;
import ru.itmo.tg.springbootcrud.labwork.dto.LabWorkResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.service.LabWorkService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lab-works")
@RequiredArgsConstructor
public class LabWorkController {

    private final LabWorkService labWorkService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public List<LabWorkResponseDTO> getLabWorks(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "20", name = "page-size", required = false) Integer pageSize,
            @RequestParam(defaultValue = "asc", required = false) String order,
            @RequestParam(defaultValue = "id", required = false) String sort) {
        return labWorkService.getLabWorks(page, pageSize, order, sort);
    }

    @GetMapping("/{id}")
    public LabWorkResponseDTO getLabWorkById(@PathVariable Long id) {
        return labWorkService.getLabWorkById(id);
    }

    @PostMapping("/create")
    public LabWorkResponseDTO createLabWork(@RequestBody LabWorkRequestDTO labWorkDTO) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.createLabWork(labWorkDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "topic/lab-works", Map.of("action", "create", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

    @PutMapping("/{id}/update")
    public LabWorkResponseDTO updateLabWork(@PathVariable Long id, @RequestBody LabWorkRequestDTO labWorkDTO) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.updateLabWork(
                id, labWorkDTO, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "topic/lab-works", Map.of("action", "update", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

    @DeleteMapping("/{id}/delete")
    public void deleteLabWork(@PathVariable Long id) {
        labWorkService.deleteLabWork(id, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "topic/lab-works", Map.of("action", "delete", "value", id));
    }

    @DeleteMapping("/delete-by-minimal-point")
    public String deleteLabWorkByMinimalPoint(@RequestParam(name = "point") Integer minimalPoint) {
        Long result = labWorkService.deleteLabWorkByMinimalPoint(minimalPoint, userService.getCurrentUser());
        if (result > 0) {
            messagingTemplate.convertAndSend(
                    "topic/lab-works", Map.of("action", "delete", "value", result));
            return "Deleted LabWork #" + result;
        }
        return "There was no LabWork assigned to current user with such minimal point";
    }

    @GetMapping("/count-by-author")
    public Integer countLabWorkByAuthor(@RequestParam(name = "author-id") Long authorId) {
        return labWorkService.getCountByAuthorId(authorId);
    }

    @GetMapping("/description-contains")
    public List<LabWorkResponseDTO> getLabWorksWithDescriptionContaining(
            @RequestParam String substring,
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "20", name = "page-size", required = false) Integer pageSize) {
        return labWorkService.getLabWorksWithDescriptionContaining(substring, page, pageSize);
    }

    @PutMapping("/{id}/adjust-difficulty")
    public LabWorkResponseDTO adjustDifficulty(@PathVariable Long id, @RequestParam Integer by) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.adjustDifficulty(
                id, by, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "topic/lab-works", Map.of("action", "update", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

    @PostMapping("/{id}/copy-to-discipline")
    public LabWorkResponseDTO copyLabWorkToDiscipline(@PathVariable Long id,
                                                      @RequestParam(name = "discipline-id") Long disciplineId) {
        LabWorkResponseDTO labWorkResponseDTO = labWorkService.copyLabWorkToDiscipline(
                id, disciplineId, userService.getCurrentUser());
        messagingTemplate.convertAndSend(
                "topic/lab-works", Map.of("action", "create", "value", labWorkResponseDTO));
        return labWorkResponseDTO;
    }

}
