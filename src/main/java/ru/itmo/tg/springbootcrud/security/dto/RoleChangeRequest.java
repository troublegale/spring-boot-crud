package ru.itmo.tg.springbootcrud.security.dto;

import ru.itmo.tg.springbootcrud.security.model.enums.Role;

public record RoleChangeRequest(Role role) {

}
