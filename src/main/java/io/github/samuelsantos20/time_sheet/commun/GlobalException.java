package io.github.samuelsantos20.time_sheet.commun;

import io.github.samuelsantos20.time_sheet.dto.ErrorField;
import io.github.samuelsantos20.time_sheet.dto.ErrorResponse;
import io.github.samuelsantos20.time_sheet.exception.DuplicateRecord;
import io.github.samuelsantos20.time_sheet.exception.OperationNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.server.ResponseStatusException;

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


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerAccessDeniedException(AccessDeniedException e) {

        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Acesso Negado!", List.of());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Erro de integridade de dados: {}", e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.value(), "Violação de integridade de dados. Verifique os dados enviados.", List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse UnexpectedOperation(RuntimeException e) {
        if (e instanceof ResponseStatusException ex) {
            throw ex; // deixa o Spring tratar com o status original (ex: 404)
        }
        log.error("Erro inesperado: ", e);

        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado, entre em contato com o suporte para maiores informações!",
                List.of()
        );
    }




}
