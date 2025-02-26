package io.github.samuelsantos20.time_sheet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name = "User")
public record UserDTO(

        @NotBlank(message = "O campo password recebeu o valor null")
        @Schema(name = "password")
        String password,

        @Schema(name = "registration")
        String registration,

        @Schema(name = "roles")
        List<String> roles
) {
}
