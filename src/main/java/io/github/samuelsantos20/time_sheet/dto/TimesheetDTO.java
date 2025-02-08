package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Approval;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record TimesheetDTO(

        @NotNull(message = "O campo userId é null!")
        User userId,

        Approval approval,

        @NotNull(message = "O campo month é null!")
        int month,

        @NotNull(message = "O campo year é null!")
        int year,

        List<WorkEntry> workEntries,

        LocalDateTime timeSheetUpdate,

        LocalDateTime timeSheetCreated,

        Duration totalHours


) {



}
