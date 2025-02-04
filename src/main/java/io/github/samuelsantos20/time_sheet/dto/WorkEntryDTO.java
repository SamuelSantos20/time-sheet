package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.model.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record WorkEntryDTO(

        Timesheet timesheet_id,

        @NotNull(message = "O campo userId é nulo!")
        User userId,

        LocalTime start_time,

        LocalTime end_time

) {



}
