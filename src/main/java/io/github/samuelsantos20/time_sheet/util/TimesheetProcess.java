package io.github.samuelsantos20.time_sheet.util;

import io.github.samuelsantos20.time_sheet.exception.OperationNotPermitted;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TimesheetProcess {

    private final TimesheetService timesheetService;

    public void process(Timesheet timesheet) {


        LocalDateTime localDateNow = LocalDateTime.now();

        YearMonth yearMonth = YearMonth.of(timesheet.getYear(), timesheet.getMonth());

        YearMonth newer = YearMonth.from(localDateNow);

        if (yearMonth.isBefore(newer)) {

            throw new OperationNotPermitted("Não é possivel relizar está operação!");

        }

        Optional<Timesheet> optionalTimesheet = timesheetService.timesheetSearchMonthAndEmployee_id(
                timesheet.getMonth(),
                timesheet.getYear(),
                timesheet.getEmployee());

        optionalTimesheet.ifPresentOrElse(timesheet1 -> {
            Timesheet existingTimesheet = optionalTimesheet.get();

            LocalTime timesheetParse = existingTimesheet.getTotalHours().plusHours(timesheet.getTotalHours().getHour()).plusMinutes(timesheet.getTotalHours().getMinute());

            existingTimesheet.setTotalHours(timesheetParse);

            timesheetService.timesheetSave(existingTimesheet);
        }, ()->{

            timesheetService.timesheetSave(timesheet);

        });
    }

}
