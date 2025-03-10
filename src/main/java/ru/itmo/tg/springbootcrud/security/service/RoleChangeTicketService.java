package ru.itmo.tg.springbootcrud.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.exception.RoleChangeTicketNotFoundException;
import ru.itmo.tg.springbootcrud.labwork.exception.TicketAlreadyResolvedException;
import ru.itmo.tg.springbootcrud.misc.UserModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeRequest;
import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.model.RoleChangeTicket;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.RoleChangeTicketStatus;
import ru.itmo.tg.springbootcrud.security.repository.RoleChangeTicketRepository;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleChangeTicketService {

    private final RoleChangeTicketRepository roleChangeTicketRepository;
    private final UserRepository userRepository;

    public List<RoleChangeTicketDTO> getAllRoleChangeRequests() {
        return UserModelDTOConverter.toRoleChangeTicketDTOList(roleChangeTicketRepository.findAll());
    }

    public RoleChangeTicketDTO getRoleChangeTicketById(Long id) {
        return UserModelDTOConverter.convert(roleChangeTicketRepository.findById(id).orElseThrow());
    }

    public List<RoleChangeTicketDTO> getRoleChangeTicketsByUser(User user) {
        return UserModelDTOConverter.toRoleChangeTicketDTOList(roleChangeTicketRepository.findByUser(user));
    }

    public RoleChangeTicketDTO createRoleChangeTicket(RoleChangeRequest roleChangeRequest, User user) {
        RoleChangeTicket ticket = RoleChangeTicket.builder()
                .user(user)
                .role(roleChangeRequest.role())
                .status(RoleChangeTicketStatus.PENDING)
                .resolver(null)
                .build();
        ticket = roleChangeTicketRepository.save(ticket);
        return UserModelDTOConverter.convert(ticket);
    }

    public RoleChangeTicketDTO approveRoleChangeTicket(Long id, User resolver) {
        RoleChangeTicket ticket = roleChangeTicketRepository.findById(id).orElseThrow(
                RoleChangeTicketNotFoundException::new);
        if (ticket.getStatus() != RoleChangeTicketStatus.PENDING) {
            throw new TicketAlreadyResolvedException();
        }
        User user = ticket.getUser();
        user.setRole(ticket.getRole());
        userRepository.save(user);
        ticket.setStatus(RoleChangeTicketStatus.APPROVED);
        ticket.setResolver(resolver);
        ticket = roleChangeTicketRepository.save(ticket);
        return UserModelDTOConverter.convert(ticket);
    }

    public RoleChangeTicketDTO rejectRoleChangeTicket(Long id, User resolver) {
        RoleChangeTicket ticket = roleChangeTicketRepository.findById(id).orElseThrow(
                RoleChangeTicketNotFoundException::new);
        if (ticket.getStatus() != RoleChangeTicketStatus.PENDING) {
            throw new TicketAlreadyResolvedException();
        }
        ticket.setStatus(RoleChangeTicketStatus.REJECTED);
        ticket.setResolver(resolver);
        ticket = roleChangeTicketRepository.save(ticket);
        return UserModelDTOConverter.convert(ticket);
    }

}
