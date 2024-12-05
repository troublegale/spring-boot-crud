package ru.itmo.tg.springbootcrud.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response with an access token")
public record JwtAuthenticationResponse(String token) {

}
