package io.github.samuelsantos20.time_sheet.commun;

import io.github.samuelsantos20.time_sheet.dto.ErrorField;
import io.github.samuelsantos20.time_sheet.dto.ErrorResponse;
import io.github.samuelsantos20.time_sheet.exception.DuplicateRecord;
import io.github.samuelsantos20.time_sheet.exception.OperationNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("Erro de Validação : {}", e.getMessage());


        List<FieldError> fileHandlers = e.getFieldErrors();

        List<ErrorField> collect = fileHandlers.stream().map(erro -> new ErrorField(erro.getField(), erro.getDefaultMessage())).collect(Collectors.toList());

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de Validação!", collect);
    }

    @ExceptionHandler(DuplicateRecord.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse DuplicateRecord(DuplicateRecord e) {

        return new ErrorResponse(HttpStatus.CONFLICT.value(), "Valor já registrado no sistema!", List.of());

    }

    @ExceptionHandler(OperationNotPermitted.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse Operationnotpermitted(OperationNotPermitted e) {

        return ErrorResponse.conflict(e.getMessage());
    }

}
