package io.github.samuelsantos20.time_sheet.validation;

import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import org.springframework.stereotype.Component;

@Component
public class WorEntryValidation {

    public void validateEntry(WorkEntry workEntry) {

        if (workEntry.getStartTime() == null) {
            throw new IllegalArgumentException("A data de entrada não pode ser nula!");
        }

    }


}

