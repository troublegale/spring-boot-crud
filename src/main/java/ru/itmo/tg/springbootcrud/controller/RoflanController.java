package ru.itmo.tg.springbootcrud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.tg.springbootcrud.service.UserService;

@RestController
@RequestMapping("/roflan")
@RequiredArgsConstructor
@Tag(name = "Roflan")
public class RoflanController {

    private final UserService userService;

    @Operation(summary = "Only for authorized users")
    @GetMapping
    public String roflan() {
        return ":roflanEbalo:";
    }

    @Operation(summary = "Only for admins")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String roflanAdmin() {
        return ":roflanEbalo: you're an admin omg";
    }

    @Operation(summary = "Promote current user to admin")
    @GetMapping("/promote")
    public void promote() {
        userService.promoteCurrent();
    }

}
