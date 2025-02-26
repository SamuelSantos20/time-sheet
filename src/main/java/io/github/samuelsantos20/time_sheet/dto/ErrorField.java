package io.github.samuelsantos20.time_sheet.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorField")
public record ErrorField (

        @Schema(name = "message")
        String message,

        @Schema(name = "campo")
        String campo){
}
