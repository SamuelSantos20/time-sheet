package io.github.samuelsantos20.time_sheet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ManagerDTO(

        @NotBlank(message = "O campo first_name é nulo!")
        String first_name,

        @NotBlank(message = "O campo last_name é nulo")
        String last_name,

        @NotBlank(message = "O campo email é nulo!")
        @Email(message = "O email digitado não é valido!")
        String email,

        @NotBlank(message = "O campo department é nulo!")
        String department) {
}
