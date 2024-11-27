package ru.itmo.tg.springbootcrud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Authentication request")
public class AuthRequest {

    @Schema(description = "Username", example = "troublegale")
    @Size(min = 3, max = 64)
    @NotNull
    private String username;

    @Schema(description = "Password", example = "$amogus_1337_FOREVER$")
    @Size(min = 8, max = 255)
    @NotNull
    private String password;

}
