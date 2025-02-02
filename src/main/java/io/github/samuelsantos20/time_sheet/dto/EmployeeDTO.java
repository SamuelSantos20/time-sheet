package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EmployeeDTO(
        @NotBlank(message = "Campo first_name é nulo!")
        String firstName,

        @NotBlank(message = "Campo last_name é nulo!")
        String lastName,

        @NotBlank(message = "O campo email é nulo!")
        @Email(message = "O email digitado não é valido!")
        String email,

        @NotBlank(message = "O campo position é nulo!")
        String position,

        @NotBlank(message = "O campo department é nulo!")
        String department,

        User user
) {
}
