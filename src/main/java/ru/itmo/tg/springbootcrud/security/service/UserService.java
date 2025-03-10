package ru.itmo.tg.springbootcrud.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.tg.springbootcrud.labwork.exception.UsernameTakenException;
import ru.itmo.tg.springbootcrud.misc.UserModelDTOConverter;
import ru.itmo.tg.springbootcrud.security.dto.UserDTO;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User createUser(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UsernameTakenException("Username " + user.getUsername() + " already taken");
        }
        return repository.save(user);
    }

    public UserDTO getByUsername(String username) {
        return UserModelDTOConverter.convert(getUserByUsername(username));
    }

    public List<UserDTO> getAllUsers() {
        return UserModelDTOConverter.toUserDTOList(repository.findAll());
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
