package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Timesheet;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record WorkEntryDTO(

        @NotNull(message = "O campo timesheet_id é nulo!")
        Timesheet timesheet_id,

        LocalTime start_time,

        LocalTime end_time

) {



}
