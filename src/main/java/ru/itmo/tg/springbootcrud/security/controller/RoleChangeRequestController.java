package ru.itmo.tg.springbootcrud.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeRequest;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.service.RoleChangeTicketService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/role-change-requests")
@RequiredArgsConstructor
public class RoleChangeRequestController {

    private final RoleChangeTicketService roleChangeTicketService;
    private final UserService userService;

    @GetMapping
    public List<RoleChangeTicketDTO> getCurrentUserRoleChangeTickets() {
        return roleChangeTicketService.getRoleChangeTicketsByUser(userService.getCurrentUser());
    }

    @PostMapping("/create")
    public RoleChangeTicketDTO createRoleChangeRequest(@RequestBody RoleChangeRequest roleChangeRequest) {
        return roleChangeTicketService.createRoleChangeTicket(roleChangeRequest, userService.getCurrentUser());
    }

}
