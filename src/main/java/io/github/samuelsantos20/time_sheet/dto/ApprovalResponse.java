package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.ApprovalStatus;
import io.github.samuelsantos20.time_sheet.model.Timesheet;

public record ApprovalResponse(ApprovalUserDTO approvalUserDTO,

                               Timesheet timesheet,

                               ApprovalStatus approvalStatus,

                               String comments
                               ) {
}
