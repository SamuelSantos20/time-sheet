package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Approval;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record TimesheetAlternativeDTO(

        @NotNull(message = "O campo userId é null!")
        String registration_user,

        Approval approval,

        @NotNull(message = "O campo month é null!")
        int month,

        @NotNull(message = "O campo year é null!")
        int year,

        LocalDateTime timeSheetUpdate,

        LocalDateTime timeSheetCreated,

        long totalHours


) {

}
