package io.github.samuelsantos20.time_sheet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "User")
public record WorkEntryDTO(

        @Schema(name = "timesheet_id")
        UUID timesheet_id,

        @NotBlank(message = "O campo registration_user é nulo!")
        @Schema(name = "registration_user")
        String registration_user,

        @Schema(name = "start_time")
        LocalDateTime start_time,

        @Schema(name = "end_time")
        LocalDateTime end_time,

        @Schema(name = "dateCreated")
        LocalDateTime dateCreated,

        @Schema(name = "dateUpdate")
        LocalDateTime dateUpdate

        )implements Serializable {


}
