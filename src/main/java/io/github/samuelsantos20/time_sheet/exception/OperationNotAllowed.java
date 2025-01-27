package io.github.samuelsantos20.time_sheet.exception;

public class OperationNotAllowed extends RuntimeException {
    public OperationNotAllowed(String message) {
        super(message);
    }
}
