package io.github.samuelsantos20.time_sheet.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(name = "Approval_Response")
public record ApprovalUserDTO(
        @Schema(name = "name")
        String name,

        @Schema(name = "registration")
        String registration)implements Serializable { }
