package io.github.samuelsantos20.time_sheet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDTO(
        @NotBlank(message = "O campo password recebeu o valor null")
        String password,

        String registration,

        List<String> roles
) {
}
