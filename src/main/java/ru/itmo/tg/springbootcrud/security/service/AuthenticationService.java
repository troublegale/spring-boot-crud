package ru.itmo.tg.springbootcrud.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.security.model.AuthRequest;
import ru.itmo.tg.springbootcrud.security.model.JwtAuthenticationResponse;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(AuthRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userService.create(user);
        String token = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(token);
    }

    public JwtAuthenticationResponse signIn(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
        ));
        UserDetails user = userService.userDetailsService().loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(token);
    }



}
