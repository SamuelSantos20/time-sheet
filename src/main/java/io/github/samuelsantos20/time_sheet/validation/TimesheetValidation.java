package io.github.samuelsantos20.time_sheet.validation;

import io.github.samuelsantos20.time_sheet.data.TimesheetData;
import io.github.samuelsantos20.time_sheet.exception.DuplicateRecord;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TimesheetValidation {

    private final TimesheetData timesheetData;


    public  void validation(Timesheet timesheet) {

        if(timesheet == null){

            throw  new IllegalArgumentException("Os dados enviados são nulos!");
        }

        if (existTimesheet(timesheet)){

            throw new DuplicateRecord("Os dados enviados já se encontram cadastrados no sistema!");

        }

    }

    private boolean existTimesheet(Timesheet timesheet) {

        Optional<Timesheet> optionalTimesheet = timesheetData.findById(timesheet.getTimesheetId());

        if (timesheet.getTimesheetId()==null){

            return optionalTimesheet.isPresent();
        }


        return optionalTimesheet.map(Timesheet::getTimesheetId).stream().anyMatch(id -> !id.equals(timesheet.getTimesheetId()));

    }

}
