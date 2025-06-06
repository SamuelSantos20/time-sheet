package io.github.samuelsantos20.time_sheet.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(name = "AuthenticationRequest")
public record AuthenticationRequest(

        @Schema(name = "username")
        String username,

        @Schema(name = "password")
        String password) implements Serializable {
}
