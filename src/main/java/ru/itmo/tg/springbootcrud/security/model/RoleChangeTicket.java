package ru.itmo.tg.springbootcrud.security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;
import ru.itmo.tg.springbootcrud.security.model.enums.RoleChangeTicketStatus;

@Entity
@Table(name = "role_change_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleChangeTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    @NotNull
    private User user;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleChangeTicketStatus status;

    @JoinColumn(name = "resolver_id")
    @ManyToOne
    private User resolver;

}
