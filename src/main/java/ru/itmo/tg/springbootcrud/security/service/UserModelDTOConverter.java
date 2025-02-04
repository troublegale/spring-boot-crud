package ru.itmo.tg.springbootcrud.security.service;

import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.security.dto.UserDTO;
import ru.itmo.tg.springbootcrud.security.model.User;

import java.util.List;

@Service
public class UserModelDTOConverter {

    public UserDTO convert(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public List<UserDTO> convert(List<User> users) {
        return users.stream().map(this::convert).toList();
    }
}
