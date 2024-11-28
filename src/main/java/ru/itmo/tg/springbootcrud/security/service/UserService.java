package ru.itmo.tg.springbootcrud.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.model.enums.Role;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User already exists");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public void grantAdmin(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRole(Role.ROLE_ADMIN);
        repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public List<User> getAllAdmins() {
        return repository.findAll()
                .stream().filter(u -> u.getRole() == Role.ROLE_ADMIN)
                .collect(Collectors.toList());
    }

    public List<User> getAllNonAdmins() {
        return repository.findAll()
                .stream().filter(u -> u.getRole() == Role.ROLE_USER)
                .collect(Collectors.toList());
    }

}
