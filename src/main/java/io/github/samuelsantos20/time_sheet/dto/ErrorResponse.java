package io.github.samuelsantos20.time_sheet.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(int status, String message, List<ErrorField> errorFields) {

    public static ErrorResponse conflict(String message) {

        return new ErrorResponse(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ErrorResponse StandardResponse(String message) {

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

}
