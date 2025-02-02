package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ManagerDTO(

        @NotBlank(message = "O campo first_name é nulo!")
        String firstName,

        @NotBlank(message = "O campo last_name é nulo")
        String lastName,

        @NotBlank(message = "O campo email é nulo!")
        @Email(message = "O email digitado não é valido!")
        String email,

        @NotBlank(message = "O campo department é nulo!")
        String department,

        User user

) {
}
