package ru.itmo.tg.springbootcrud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.tg.springbootcrud.dto.AuthRequest;
import ru.itmo.tg.springbootcrud.dto.JwtAuthenticationResponse;
import ru.itmo.tg.springbootcrud.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "User registration")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid AuthRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "User logging in")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid AuthRequest request) {
        return authenticationService.signIn(request);
    }

}
