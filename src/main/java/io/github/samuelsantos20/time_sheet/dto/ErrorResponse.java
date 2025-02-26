package io.github.samuelsantos20.time_sheet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.List;

@Schema(name = "ErrorResponse")
public record ErrorResponse(
        @Schema(name = "status")
        int status,

        @Schema(name = "message")
        String message,

        @Schema(name = "errorFields")
        List<ErrorField> errorFields) {

    public static ErrorResponse conflict(String message) {

        return new ErrorResponse(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ErrorResponse StandardResponse(String message) {

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

}
