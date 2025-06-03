package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Schema(name = "Employee")
public record EmployeeDTO(

        @NotBlank(message = "Campo first_name é nulo!")
        @Schema(name = "firstName")
        String firstName,

        @NotBlank(message = "Campo last_name é nulo!")
        @Schema(name = "lastName")
        String lastName,

        @NotBlank(message = "O campo email é nulo!")
        @Email(message = "O email digitado não é valido!")
        @Schema(name = "email")
        String email,

        @NotBlank(message = "O campo position é nulo!")
        @Schema(name = "position")
        String position,

        @NotBlank(message = "O campo department é nulo!")
        @Schema(name = "department")
        String department,

        User user
) implements Serializable {
}
