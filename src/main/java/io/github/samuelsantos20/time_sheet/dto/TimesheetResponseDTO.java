package io.github.samuelsantos20.time_sheet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(name = "TimesheetResponseDTO")
public record TimesheetResponseDTO(

        @NotNull(message = "O campo userId é null!")
        @Schema(name = "registration_user")
        String registration_user,

        @NotNull(message = "O campo month é null!")
        @Schema(name = "month")
        int month,

        @NotNull(message = "O campo year é null!")
        @Schema(name = "year")
        int year,

        LocalDateTime timeSheetUpdate,

        LocalDateTime timeSheetCreated,

        @Schema(name = "totalHours")
        long totalHours


) {

}
