package ru.itmo.tg.springbootcrud.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.tg.springbootcrud.security.model.AuthRequest;
import ru.itmo.tg.springbootcrud.security.model.JwtAuthenticationResponse;
import ru.itmo.tg.springbootcrud.security.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

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
