package io.github.samuelsantos20.time_sheet.exception;

public class DuplicateRecord extends RuntimeException {
    public DuplicateRecord(String message) {
        super(message);
    }
}
