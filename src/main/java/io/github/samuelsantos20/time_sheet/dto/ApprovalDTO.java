package io.github.samuelsantos20.time_sheet.dto;

import io.github.samuelsantos20.time_sheet.model.ApprovalStatus;
import io.github.samuelsantos20.time_sheet.model.Manager;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Approval")
public record ApprovalDTO (

        @NotNull(message = "É necessario que seja indicado o manager da tarefa!")
        @Schema(name = "idUser")
        UUID idUser,

        @NotNull(message = "É necessario que haja a indicação da timesheet!")
        @Schema(name = "idTimesheet")
        UUID idTimesheet,

        @NotNull(message = "É necessario que haja algun status para a folha(PENDING,APPROVED,REJECTED)!")
        @Schema(name = "approvalStatus")
        ApprovalStatus approvalStatus,

        LocalDateTime approval_date_created,

        LocalDateTime approval_date_update,

        @NotBlank(message = "É necessario que haja algun comment!")
        @Schema(name = "comments")
        String comments) implements Serializable{




}
