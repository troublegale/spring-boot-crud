package ru.itmo.tg.springbootcrud.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.dto.UserDTO;
import ru.itmo.tg.springbootcrud.security.service.RoleChangeService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Administration")
public class AdminController {

    private final UserService userService;
    private final RoleChangeService roleChangeService;

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
        return roleChangeService.getAllRoleChangeRequests();
    }

    @GetMapping("/tickets/{id}")
    public RoleChangeTicketDTO getRoleChangeTicketById(@PathVariable Long id) {
        return roleChangeService.getRoleChangeTicketById(id);
    }

    @PutMapping("/tickets/{id}/approve")
    public void approveRoleChangeTicket(@PathVariable Long id) {
        roleChangeService.approveRoleChangeTicket(id, userService.getCurrentUser());
    }

    @PutMapping("/tickets/{id}/reject")
    public void rejectRoleChangeTicket(@PathVariable Long id) {
        roleChangeService.rejectRoleChangeTicket(id, userService.getCurrentUser());
    }

}
