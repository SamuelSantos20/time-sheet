package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.TimesheetData;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import io.github.samuelsantos20.time_sheet.validation.TimesheetValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class TimesheetService {

    private final TimesheetData timesheetData;

    private final TimesheetValidation timesheetValidation;

    public  void timesheetSave(Timesheet timesheet) {

        timesheetValidation.validation(timesheet);

        timesheetData.save(timesheet);

    }

    public Optional<Timesheet>timesheetSearch(UUID id) {

        return Optional.ofNullable(timesheetData.findById(id).orElseThrow(() -> new IllegalArgumentException("O valor digitado não foi localizado!")));
    }

    public List<Timesheet> TimesheetList() {

        return timesheetData.findAll();

    }

    public Optional<Timesheet> timesheetSearchMonthAndEmployee_id(int month,
                                                                  int year,
                                                                  Employee employee_id) {


        return timesheetData.findByMonthAndYearAndEmployee_id(month, year, employee_id);
    }


    public List<Timesheet> ListFindByMonthAndEmployee_id(int month, int year,Employee employee_id) {

        return timesheetData.ListFindByMonthAndEmployee(month, year, employee_id);
    }


    public void timesheetUpdate(Timesheet timesheet) {

        timesheetValidation.validation(timesheet);

        timesheetData.save(timesheet);
    }

    public  void timesheetDelete(UUID id) {
        if (id == null){

            throw new IllegalArgumentException("O valor digitado é nulo");
        }

        timesheetData.deleteById(id);
    }

}
