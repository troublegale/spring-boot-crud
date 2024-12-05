package ru.itmo.tg.springbootcrud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;
import ru.itmo.tg.springbootcrud.security.model.enums.RoleChangeTicketStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleChangeTicketDTO {

    private Long id;

    private String username;

    private Role role;

    private RoleChangeTicketStatus status;

    private String resolverUsername;

}
