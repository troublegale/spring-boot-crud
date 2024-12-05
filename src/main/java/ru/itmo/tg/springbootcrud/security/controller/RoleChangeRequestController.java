package ru.itmo.tg.springbootcrud.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeRequest;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.service.RoleChangeService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/role-change-requests")
@RequiredArgsConstructor
public class RoleChangeRequestController {

    private final RoleChangeService roleChangeService;
    private final UserService userService;

    @GetMapping
    public List<RoleChangeTicketDTO> getCurrentUserRoleChangeTickets() {
        return roleChangeService.getRoleChangeTicketsByUser(userService.getCurrentUser());
    }

    @PostMapping("/create")
    public void createRoleChangeRequest(@RequestBody RoleChangeRequest roleChangeRequest) {
        roleChangeService.createRoleChangeTicket(roleChangeRequest, userService.getCurrentUser());
    }

}
