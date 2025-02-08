package io.github.samuelsantos20.time_sheet.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkEntryDTO(

        UUID timesheet_id,

        @NotBlank(message = "O campo registration_user é nulo!")
        String registration_user,

        LocalDateTime start_time,

        LocalDateTime end_time,

        LocalDateTime dateCreated,

        LocalDateTime dateUpdate

        ) {


}
