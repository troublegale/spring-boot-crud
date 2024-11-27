package ru.itmo.tg.springbootcrud.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {

    @Size(min = 3, max = 64)
    @NotNull
    private String username;

    @Size(min = 8, max = 255)
    @NotNull
    private String password;

}
