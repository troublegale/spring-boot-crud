package ru.itmo.tg.springbootcrud.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String username;

    private Role role;

}
