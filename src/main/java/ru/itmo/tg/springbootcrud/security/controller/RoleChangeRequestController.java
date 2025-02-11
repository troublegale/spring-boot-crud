package ru.itmo.tg.springbootcrud.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeRequest;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.service.RoleChangeTicketService;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/role-change-requests")
@RequiredArgsConstructor
@Tag(name = "User's Role Change Requests")
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class RoleChangeRequestController {

    private final RoleChangeTicketService roleChangeTicketService;
    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get User's Role Change Requests",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Role Change Requests retrieved", content = @Content(schema = @Schema(implementation = RoleChangeTicketDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    public List<RoleChangeTicketDTO> getCurrentUserRoleChangeTickets() {
        return roleChangeTicketService.getRoleChangeTicketsByUser(userService.getCurrentUser());
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Pend a Role Change Request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role Change Request created as a ticket", content = @Content(schema = @Schema(implementation = RoleChangeTicketDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
                    @ApiResponse(responseCode = "400", description = "Invalid request structure")
            }
    )
    public RoleChangeTicketDTO createRoleChangeRequest(@RequestBody RoleChangeRequest roleChangeRequest) {
        return roleChangeTicketService.createRoleChangeTicket(roleChangeRequest, userService.getCurrentUser());
    }

}
