package ru.itmo.tg.springbootcrud.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.security.dto.AuthRequest;
import ru.itmo.tg.springbootcrud.security.dto.JwtAuthenticationResponse;
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
        Role role = userService.createUser(user).getRole();
        String token = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(token, role);
    }

    public JwtAuthenticationResponse signIn(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
        ));
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(request.getUsername());
        User user = (User) userDetails;
        Role role = user.getRole();
        String token = jwtService.generateToken(userDetails);
        return new JwtAuthenticationResponse(token, role);
    }

}
