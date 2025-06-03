package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Approval;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "Timesheet")
public record TimesheetDTO(

        @NotNull(message = "O campo userId é null!")
        @Schema(name = "userId")
        User userId,

        @Schema(name = "approval")
        Approval approval,

        @NotNull(message = "O campo month é null!")
        @Schema(name = "month")
        int month,

        @NotNull(message = "O campo year é null!")
        @Schema(name = "year")
        int year,

        @Schema(name = "workEntries")
        List<WorkEntry> workEntries,

        @Schema(name = "timeSheetUpdate")
        LocalDateTime timeSheetUpdate,

        @Schema(name = "timeSheetCreated")
        LocalDateTime timeSheetCreated,

        @Schema(name = "totalHours")
        Duration totalHours


) implements Serializable {



}
