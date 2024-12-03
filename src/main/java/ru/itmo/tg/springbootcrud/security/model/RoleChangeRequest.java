package ru.itmo.tg.springbootcrud.security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;
import ru.itmo.tg.springbootcrud.security.model.enums.RoleChangeRequestStatus;

@Entity
@Table(name = "role_change_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleChangeRequestStatus status;

    @JoinColumn(name = "resolver_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User resolvedBy;

}
