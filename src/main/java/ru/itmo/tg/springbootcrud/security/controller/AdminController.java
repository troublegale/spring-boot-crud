package ru.itmo.tg.springbootcrud.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineResponseDTO;
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
@Tag(name = "Administration", description = "Admin operations")
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class AdminController {

    private final UserService userService;
    private final RoleChangeTicketService roleChangeTicketService;
    private final UpdateHistoryService updateHistoryService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all Users",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Users retrieved", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get User by ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User object retrieved", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @Operation(summary = "Get all Role Change Tickets",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Array of Role Change Tickets retrieved", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    @GetMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleChangeTicketDTO> getRoleChangeTickets() {
        return roleChangeTicketService.getAllRoleChangeRequests();
    }

    @GetMapping(value = "/tickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Role Change Ticket by ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Role Change Ticket object retrieved", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    public RoleChangeTicketDTO getRoleChangeTicketById(@PathVariable Long id) {
        return roleChangeTicketService.getRoleChangeTicketById(id);
    }

    @PutMapping(value = "/tickets/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Approve a Role Change Ticket by ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket approved retrieved", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions or already resolved"),
                    @ApiResponse(responseCode = "404", description = "Ticket with stated ID not found")
            }
    )
    public RoleChangeTicketDTO approveRoleChangeTicket(@PathVariable Long id) {
        return roleChangeTicketService.approveRoleChangeTicket(id, userService.getCurrentUser());
    }

    @PutMapping(value = "/tickets/{id}/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Reject a Role Change Ticket by ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ticket rejected", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions or already resolved"),
                    @ApiResponse(responseCode = "404", description = "Ticket with stated ID not found")
            }
    )
    public RoleChangeTicketDTO rejectRoleChangeTicket(@PathVariable Long id) {
        return roleChangeTicketService.rejectRoleChangeTicket(id, userService.getCurrentUser());
    }

    @GetMapping(value = "/lab-work-history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get full Update History of all Lab Works",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Update History retrieved", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    public List<UpdateHistoryDTO> getAllUpdateHistory() {
        return updateHistoryService.getAllUpdateHistory();
    }

    @GetMapping("/lab-work-history/{lab-id}")
    @Operation(summary = "Get Update History of a Lab Work with stated ID",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lab Work's Update History retrieved", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
                    @ApiResponse(responseCode = "404", description = "Lab Work with stated ID not found")
            }
    )
    public UpdateHistoryDTO getLabWorkUpdateHistory(@PathVariable(name = "lab-id") Long labId) {
        return updateHistoryService.getLabWorkUpdateHistory(labId);
    }

}
