package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Timesheet;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record EmployeeDTO(
        List<Timesheet> timesheets,

        @NotBlank(message = "Campo first_name é nulo!")
        String first_name,

        @NotBlank(message = "Campo last_name é nulo!")
        String last_name,

        @NotBlank(message = "O campo email é nulo!")
        @Email(message = "O email digitado não é valido!")
        String email,

        @NotBlank(message = "O campo position é nulo!")
        String position,

        @NotBlank(message = "O campo department é nulo!")
        String department,



        @NotBlank(message = "O campo registration é nulo!")
        String registration) {
}
