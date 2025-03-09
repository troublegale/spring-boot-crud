package ru.itmo.tg.springbootcrud.security.service;

import ru.itmo.tg.springbootcrud.security.dto.RoleChangeTicketDTO;
import ru.itmo.tg.springbootcrud.security.dto.UserDTO;
import ru.itmo.tg.springbootcrud.security.model.RoleChangeTicket;
import ru.itmo.tg.springbootcrud.security.model.User;

import java.util.List;

public class UserModelDTOConverter {

    public static UserDTO convert(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public static RoleChangeTicketDTO convert(RoleChangeTicket roleChangeTicket) {
        return RoleChangeTicketDTO.builder()
                .id(roleChangeTicket.getId())
                .username(roleChangeTicket.getUser().getUsername())
                .role(roleChangeTicket.getRole())
                .status(roleChangeTicket.getStatus())
                .resolverUsername(
                        roleChangeTicket.getResolver() == null ? null : roleChangeTicket.getResolver().getUsername())
                .build();
    }

    public static List<UserDTO> toUserDTOList(List<User> users) {
        return users.stream().map(UserModelDTOConverter::convert).toList();
    }

    public static List<RoleChangeTicketDTO> toRoleChangeTicketDTOList(List<RoleChangeTicket> roleChangeTickets) {
        return roleChangeTickets.stream().map(UserModelDTOConverter::convert).toList();
    }
}
