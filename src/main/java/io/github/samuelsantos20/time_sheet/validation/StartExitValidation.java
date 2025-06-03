package io.github.samuelsantos20.time_sheet.validation;

import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import io.github.samuelsantos20.time_sheet.service.WorkEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartExitValidation {
    private final WorkEntryService workEntryService;

    public ResponseEntity<Object> validateWorkEntryAndExit(WorkEntry workEntry) {
        Optional<WorkEntry> workEntry1 = workEntryService.existsByStartTimeAndEndTime(
                workEntry.getStartTime(),
                workEntry.getEndTime(),
                workEntry.getUserId().getId()
        );

        if (workEntry1.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Já existe um trabalho registrado nesta data!");
        }

        return ResponseEntity.ok().build();
    }
}
