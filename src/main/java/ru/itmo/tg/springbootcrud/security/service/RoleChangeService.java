package ru.itmo.tg.springbootcrud.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeRequest;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.model.RoleChangeTicket;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.RoleChangeTicketStatus;
import ru.itmo.tg.springbootcrud.security.repository.RoleChangeTicketRepository;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleChangeService {

    private final RoleChangeTicketRepository roleChangeTicketRepository;
    private final UserRepository userRepository;

    public List<RoleChangeTicketDTO> getAllRoleChangeRequests() {
        return toDTOList(roleChangeTicketRepository.findAll());
    }

    public RoleChangeTicketDTO getRoleChangeTicketById(Long id) {
        return toDTO(roleChangeTicketRepository.findById(id).orElseThrow());
    }

    public List<RoleChangeTicketDTO> getRoleChangeTicketsByUser(User user) {
        return toDTOList(roleChangeTicketRepository.findByUser(user));
    }

    public void createRoleChangeTicket(RoleChangeRequest roleChangeRequest, User user) {
        RoleChangeTicket ticket = RoleChangeTicket.builder()
                .user(user)
                .role(roleChangeRequest.role())
                .status(RoleChangeTicketStatus.PENDING)
                .resolver(null)
                .build();
        roleChangeTicketRepository.save(ticket);
    }

    public void approveRoleChangeTicket(Long id, User resolver) {
        RoleChangeTicket ticket = roleChangeTicketRepository.findById(id).orElseThrow();
        User user = ticket.getUser();
        user.setRole(ticket.getRole());
        userRepository.save(user);
        ticket.setStatus(RoleChangeTicketStatus.APPROVED);
        ticket.setResolver(resolver);
        roleChangeTicketRepository.save(ticket);
    }

    public void rejectRoleChangeTicket(Long id, User resolver) {
        RoleChangeTicket ticket = roleChangeTicketRepository.findById(id).orElseThrow();
        ticket.setStatus(RoleChangeTicketStatus.REJECTED);
        ticket.setResolver(resolver);
        roleChangeTicketRepository.save(ticket);
    }

    private RoleChangeTicketDTO toDTO(RoleChangeTicket roleChangeTicket) {
        return RoleChangeTicketDTO.builder()
                .id(roleChangeTicket.getId())
                .username(roleChangeTicket.getUser().getUsername())
                .role(roleChangeTicket.getRole())
                .status(roleChangeTicket.getStatus())
                .resolverUsername(
                        roleChangeTicket.getResolver() == null ? null : roleChangeTicket.getResolver().getUsername())
                .build();
    }

    private List<RoleChangeTicketDTO> toDTOList(List<RoleChangeTicket> roleChangeTickets) {
        return roleChangeTickets.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
