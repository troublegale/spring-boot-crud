package ru.itmo.tg.springbootcrud.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.security.ModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.dto.UserDTO;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final ModelDTOConverter modelDTOConverter;

    public void createUser(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User already exists");
        }
        repository.save(user);
    }

    public UserDTO getByUsername(String username) {
        return modelDTOConverter.convert(getUserByUsername(username));
    }

    public List<UserDTO> getAllUsers() {
        return modelDTOConverter.convert(repository.findAll());
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUsername(username).orElse(null);
    }

    public UserDetailsService userDetailsService() {
        return this::getUserByUsername;
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
