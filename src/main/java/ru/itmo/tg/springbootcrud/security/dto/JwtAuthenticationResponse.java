package ru.itmo.tg.springbootcrud.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

@Schema(description = "Response with an access token")
public record JwtAuthenticationResponse(String token, Role role) {

}
