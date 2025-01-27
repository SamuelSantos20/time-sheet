package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.Approval;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public record TimesheetDTO(
        @NotNull(message = "O campo employee_id é nulo!")
        Employee employee_id,


        List<WorkEntry> workEntries,

        Approval approval,

        @NotNull(message = "O campo time_sheet_update é nulo!")
        LocalTime time_sheet_update,

        @NotNull(message = "O campo total_hours é nulo!")
        LocalTime total_hours

) {
}
