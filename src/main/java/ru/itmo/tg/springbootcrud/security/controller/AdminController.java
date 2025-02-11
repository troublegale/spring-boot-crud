package ru.itmo.tg.springbootcrud.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.UpdateHistoryDTO;
import ru.itmo.tg.springbootcrud.labwork.service.UpdateHistoryService;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.dto.UserDTO;
import ru.itmo.tg.springbootcrud.security.service.RoleChangeTicketService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Administration")
public class AdminController {

    private final UserService userService;
    private final RoleChangeTicketService roleChangeTicketService;
    private final UpdateHistoryService updateHistoryService;

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/tickets")
    public List<RoleChangeTicketDTO> getRoleChangeTickets() {
        return roleChangeTicketService.getAllRoleChangeRequests();
    }

    @GetMapping("/tickets/{id}")
    public RoleChangeTicketDTO getRoleChangeTicketById(@PathVariable Long id) {
        return roleChangeTicketService.getRoleChangeTicketById(id);
    }

    @PutMapping("/tickets/{id}/approve")
    public RoleChangeTicketDTO approveRoleChangeTicket(@PathVariable Long id) {
        return roleChangeTicketService.approveRoleChangeTicket(id, userService.getCurrentUser());
    }

    @PutMapping("/tickets/{id}/reject")
    public RoleChangeTicketDTO rejectRoleChangeTicket(@PathVariable Long id) {
        return roleChangeTicketService.rejectRoleChangeTicket(id, userService.getCurrentUser());
    }

    @GetMapping("/lab-work-history")
    public List<UpdateHistoryDTO> getAllUpdateHistory() {
        return updateHistoryService.getAllUpdateHistory();
    }

    @GetMapping("/lab-work-history/{lab-id}")
    public UpdateHistoryDTO getLabWorkUpdateHistory(@PathVariable(name = "lab-id") Long labId) {
        return updateHistoryService.getLabWorkUpdateHistory(labId);
    }

}
