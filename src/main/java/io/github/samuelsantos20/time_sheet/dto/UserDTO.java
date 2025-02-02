package io.github.samuelsantos20.time_sheet.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank(message = "O campo password recebeu o valor null")
        String password,

        @NotBlank(message = "O campo registration recebeu o valor null")
        String registration

) {
}
