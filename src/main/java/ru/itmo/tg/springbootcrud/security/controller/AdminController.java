package ru.itmo.tg.springbootcrud.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.tg.springbootcrud.security.model.User;
import ru.itmo.tg.springbootcrud.security.service.UserService;

import java.util.List;
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Administration")
public class AdminController {

    private final UserService userService;

    @PutMapping("/users/{username}/grant-admin")
    public ResponseEntity<String> grantAdmin(@PathVariable String username) {
        userService.grantAdmin(username);
        return ResponseEntity.ok("User granted admin rights.");
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

}
