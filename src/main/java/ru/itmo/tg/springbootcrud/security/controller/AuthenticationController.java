package ru.itmo.tg.springbootcrud.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.labwork.dto.DisciplineResponseDTO;
import ru.itmo.tg.springbootcrud.labwork.exception.PasswordTooShortException;
import ru.itmo.tg.springbootcrud.security.dto.AuthRequest;
import ru.itmo.tg.springbootcrud.security.dto.JwtAuthenticationResponse;
import ru.itmo.tg.springbootcrud.security.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "User registration",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered", content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Username already taken"),
                    @ApiResponse(responseCode = "400", description = "Invalid request structure or password is too short")
            }
    )
    public JwtAuthenticationResponse signUp(@RequestBody AuthRequest request) {
        return authenticationService.signUp(request);
    }


    @PostMapping(value = "/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "User logging in",
    responses = {
            @ApiResponse(responseCode = "200", description = "User signed in", content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
            @ApiResponse(responseCode = "403", description = "Bad credentials or not registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request structure or password is too short")
    })
    public JwtAuthenticationResponse signIn(@RequestBody @Valid AuthRequest request) {
        return authenticationService.signIn(request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handle(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
