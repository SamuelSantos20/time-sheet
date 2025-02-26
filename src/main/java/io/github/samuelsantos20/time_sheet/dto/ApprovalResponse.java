package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.ApprovalStatus;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Approval_Response")
public record ApprovalResponse(

        @Schema(name = "approvalUserDTO")
        ApprovalUserDTO approvalUserDTO,

        @Schema(name = "timesheet")
        Timesheet timesheet,

        @Schema(name = "approvalStatus")
        ApprovalStatus approvalStatus,

        @Schema(name = "comments")
        String comments
                               ) {
}
